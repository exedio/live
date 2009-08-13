/*
 * Copyright (C) 2004-2009  exedio GmbH (www.exedio.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.exedio.cope.live;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.exedio.cope.Feature;
import com.exedio.cope.IntegerField;
import com.exedio.cope.Item;
import com.exedio.cope.Model;
import com.exedio.cope.NoSuchIDException;
import com.exedio.cope.StringField;
import com.exedio.cope.Type;
import com.exedio.cope.pattern.History;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.util.ConnectToken;
import com.exedio.cope.util.ServletUtil;
import com.exedio.cops.Cop;
import com.exedio.cops.CopsServlet;
import com.exedio.cops.Resource;

public abstract class Editor extends CopsServlet
{
	static final Resource logo = new Resource("logo.png");
	
	private final Model model;
	
	/**
	 * Subclasses must define a public no-args constructor
	 * providing the model.
	 */
	protected Editor(final Model model)
	{
		if(model==null)
			throw new NullPointerException("model");
		
		this.model = model;
	}
	
	private ServletConfig config = null;
	private boolean draftsEnabled = false;
	private Target defaultTarget = TargetLive.INSTANCE;
	private ConnectToken connectToken = null;
	private final Object connectTokenLock = new Object();
	
	@Override
	public final void init(final ServletConfig config) throws ServletException
	{
		super.init();
		
		this.config = config;
		for(final Type<?> type : model.getTypes())
			if(type==DraftItem.TYPE) // DraftItem implies Draft because of the parent field
			{
				draftsEnabled = true;
				defaultTarget = TargetNewDraft.INSTANCE;
				break;
			}
	}
	
	private final void startTransaction(final String name)
	{
		synchronized(connectTokenLock)
		{
			if(connectToken==null)
			{
				connectToken = ServletUtil.connect(model, config, getClass().getName());
				model.reviseIfSupported();
			}
		}
		model.startTransaction(getClass().getName() + '#' + name);
	}
	
	@Override
	public final void destroy()
	{
		synchronized(connectTokenLock)
		{
			if(connectToken!=null)
			{
				connectToken.returnIt();
				connectToken = null;
			}
		}
		
		super.destroy();
	}
	
	protected abstract Session login(String user, String password);
	
	@SuppressWarnings("unused")
	protected String getBorderButtonURL(HttpServletRequest request, HttpServletResponse response, boolean bordersEnabled)
	{
		return null;
	}
	
	protected abstract String getHideButtonURL(HttpServletRequest request, HttpServletResponse response);
	
	@SuppressWarnings("unused")
	protected String getCloseButtonURL(HttpServletRequest request, HttpServletResponse response)
	{
		return null;
	}
	
	@SuppressWarnings("unused")
	protected String getPreviousPositionButtonURL(HttpServletRequest request, HttpServletResponse response)
	{
		return null;
	}
	
	@Override
	public final void doRequest(
			final HttpServletRequest request,
			final HttpServletResponse response)
	throws IOException
	{
		request.setCharacterEncoding(UTF8);
		final HttpSession httpSession = request.getSession(true);
		final Object anchor = httpSession.getAttribute(ANCHOR);
		
		if(anchor==null)
			doLogin(request, httpSession, response);
		else
		{
			if(request.getParameter(PREVIEW_OVERVIEW)!=null)
				doPreviewOverview(request, response, (Anchor)anchor);
			else if(request.getParameter(MEDIA_FEATURE)!=null)
				doMedia(request, response, (Anchor)anchor);
			else
				doBar(request, httpSession, response, (Anchor)anchor);
		}
	}
	
	private static final void redirectHome(
			final HttpServletRequest request,
			final HttpServletResponse response)
	throws IOException
	{
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/edited")); // TODO
	}
	
	static final String AVOID_COLLISION = "contentEditorBar823658617";
	static final String BAR_REFERER = "referer";
	static final String BAR_BORDERS_ON  = "borders.on";
	static final String BAR_BORDERS_OFF = "borders.off";
	static final String BAR_CLOSE = "close";
	static final String BAR_SWITCH_TARGET = "target.switch";
	static final String BAR_SAVE_TARGET   = "target.save";
	
	static final String BAR_FEATURE = "feature";
	static final String BAR_ITEM    = "item";
	static final String BAR_TEXT    = "text";
	static final String BAR_FILE    = "file";
	static final String BAR_ITEM_FROM = "itemPrevious";
	static final String BAR_PUBLISH_NOW = "publishNow";
	
	private static final String BAR_CLOSE_IMAGE       = BAR_CLOSE       + ".x";
	private static final String BAR_BORDERS_ON_IMAGE  = BAR_BORDERS_ON  + ".x";
	private static final String BAR_BORDERS_OFF_IMAGE = BAR_BORDERS_OFF + ".x";
	
	@SuppressWarnings("deprecation")
	private static final boolean isMultipartContent(final HttpServletRequest request)
	{
		return ServletFileUpload.isMultipartContent(request);
	}
	
	static final String PREVIEW_OVERVIEW = "po";
	static final String MODIFICATION_PUBLISH = "modification.publish";
	static final String MODIFICATION_DISCARD = "modification.discard";
	static final String MODIFICATION_PERSIST = "modification.persist";
	static final String MODIFICATION_PERSIST_COMMENT = "modification.persistComment";
	static final String MODIFICATION_IDS = "id";
	static final String SAVE_TO_DRAFT = "draft.saveTo";
	static final String DRAFT_ID     = "draft.id";
	static final String DRAFT_LOAD   = "draft.load";
	static final String DRAFT_DELETE = "draft.delete";
	static final String DRAFT_NEW    = "draft.new";
	static final String DRAFT_COMMENT= "draft.comment";
	static final String TARGET_ID   = "target.id";
	static final String TARGET_OPEN = "target.load";
	
	private final void doPreviewOverview(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final Anchor anchor)
	throws IOException
	{
		if(Cop.isPost(request))
		{
			final String[] idA = request.getParameterValues(MODIFICATION_IDS);
			final HashSet<String> ids = idA!=null ? new HashSet<String>(Arrays.asList(idA)) : null;
			if(request.getParameter(MODIFICATION_PUBLISH)!=null)
			{
				try
				{
					startTransaction("publishPreviews");
					for(final Iterator<Modification> i = anchor.modifications.iterator(); i.hasNext(); )
					{
						final Modification p = i.next();
						if(ids!=null && ids.contains(p.getID()))
						{
							p.publish();
							i.remove();
						}
					}
					// TODO maintain history
					model.commit();
				}
				finally
				{
					model.rollbackIfNotCommitted();
				}
			}
			else if(request.getParameter(MODIFICATION_PERSIST)!=null)
			{
				try
				{
					startTransaction("persistProposals");
					final Draft parent = new Draft(anchor.user, anchor.sessionName, request.getParameter(MODIFICATION_PERSIST_COMMENT));
					for(final Iterator<Modification> i = anchor.modifications.iterator(); i.hasNext(); )
					{
						final Modification p = i.next();
						if(ids!=null && ids.contains(p.getID()))
						{
							p.saveTo(parent);
							i.remove();
						}
					}
					model.commit();
				}
				finally
				{
					model.rollbackIfNotCommitted();
				}
			}
			else if(request.getParameter(SAVE_TO_DRAFT)!=null)
			{
				try
				{
					startTransaction("saveToDraft");
					final Draft parent = (Draft)model.getItem(request.getParameter(DRAFT_ID));
					for(final Iterator<Modification> i = anchor.modifications.iterator(); i.hasNext(); )
					{
						final Modification p = i.next();
						if(ids!=null && ids.contains(p.getID()))
						{
							p.saveTo(parent);
							i.remove();
						}
					}
					model.commit();
				}
				catch(NoSuchIDException e)
				{
					throw new RuntimeException(e);
				}
				finally
				{
					model.rollbackIfNotCommitted();
				}
			}
			else if(request.getParameter(MODIFICATION_DISCARD)!=null)
			{
				for(final Iterator<Modification> i = anchor.modifications.iterator(); i.hasNext(); )
				{
					final Modification p = i.next();
					if(ids!=null && ids.contains(p.getID()))
						i.remove();
				}
			}
			else if(request.getParameter(DRAFT_LOAD)!=null)
			{
				try
				{
					startTransaction("loadDraft");
					final Draft draft =
						(Draft)model.getItem(request.getParameter(DRAFT_ID));
					for(final DraftItem i : draft.getItems())
						anchor.modify(
								DraftItem.newValue.get(i),
								(StringField)model.getFeature(DraftItem.feature.get(i)),
								model.getItem(DraftItem.item.get(i)));
				}
				catch(NoSuchIDException e)
				{
					throw new RuntimeException(e);
				}
				finally
				{
					model.rollbackIfNotCommitted();
				}
			}
			else if(request.getParameter(DRAFT_DELETE)!=null)
			{
				try
				{
					startTransaction("deleteDraft");
					((Draft)model.getItem(request.getParameter(DRAFT_ID))).deleteCopeItem();
					model.commit();
				}
				catch(NoSuchIDException e)
				{
					throw new RuntimeException(e);
				}
				finally
				{
					model.rollbackIfNotCommitted();
				}
			}
			else if(request.getParameter(DRAFT_NEW)!=null)
			{
				try
				{
					startTransaction("newDraft");
					new Draft(anchor.user, anchor.sessionName, request.getParameter(DRAFT_COMMENT));
					model.commit();
				}
				finally
				{
					model.rollbackIfNotCommitted();
				}
			}
			else if(request.getParameter(TARGET_OPEN)!=null)
			{
				anchor.setTarget(getTarget(request.getParameter(TARGET_ID)));
			}
		}
		
		final StringBuilder out = new StringBuilder();
		try
		{
			startTransaction("proposal");
			final List<Draft> drafts =
				draftsEnabled
				? Draft.TYPE.search(null, Draft.date, true)
				: null;
			final ArrayList<Target> targets = new ArrayList<Target>();
			targets.add(TargetLive.INSTANCE);
			if(draftsEnabled)
			{
				for(final Draft draft : drafts)
					targets.add(new TargetDraft(draft));
				targets.add(TargetNewDraft.INSTANCE);
			}
			Preview_Jspm.writeOverview(
					out,
					request, response,
					response.encodeURL(request.getContextPath() + request.getServletPath() + '?' + PREVIEW_OVERVIEW + "=t"),
					anchor.getModifications(),
					anchor.getTarget(), targets,
					draftsEnabled, drafts);
			model.commit();
		}
		finally
		{
			model.rollbackIfNotCommitted();
		}
		
		response.setContentType("text/html; charset="+UTF8);
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.addHeader("Cache-Control", "max-age=0");
		response.addHeader("Cache-Control", "must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", System.currentTimeMillis());
		writeBody(out, response);
	}
	
	private final Target getTarget(final String id)
	{
		if(TargetLive.ID.equals(id))
		{
			return TargetLive.INSTANCE;
		}
		else if(TargetNewDraft.ID.equals(id))
		{
			return TargetNewDraft.INSTANCE;
		}
		else
		{
			try
			{
				startTransaction("findDraft");
				return new TargetDraft((Draft)model.getItem(id));
			}
			catch(NoSuchIDException e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
				model.rollbackIfNotCommitted();
			}
		}
	}
	
	private final void doBar(
			final HttpServletRequest request,
			final HttpSession httpSession,
			final HttpServletResponse response,
			final Anchor anchor)
	throws IOException
	{
		if(!Cop.isPost(request))
		{
			redirectHome(request, response);
			return;
		}
		
		final String referer;
		
		if(isMultipartContent(request))
		{
			final HashMap<String, String> fields = new HashMap<String, String>();
			final HashMap<String, FileItem> files = new HashMap<String, FileItem>();
			final FileItemFactory factory = new DiskFileItemFactory();
			final ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding(UTF8);
			try
			{
				for(Iterator<?> i = upload.parseRequest(request).iterator(); i.hasNext(); )
				{
					final FileItem item = (FileItem)i.next();
					if(item.isFormField())
						fields.put(item.getFieldName(), item.getString(UTF8));
					else
						files.put(item.getFieldName(), item);
				}
			}
			catch(FileUploadException e)
			{
				throw new RuntimeException(e);
			}
			
			final String featureID = fields.get(BAR_FEATURE);
			if(featureID==null)
				throw new NullPointerException();
			
			final Media feature = (Media)model.getFeature(featureID);
			if(feature==null)
				throw new NullPointerException(featureID);
			
			final String itemID = fields.get(BAR_ITEM);
			if(itemID==null)
				throw new NullPointerException();
			
			final FileItem file = files.get(BAR_FILE);
		
			try
			{
				startTransaction("publishFile(" + featureID + ',' + itemID + ')');
				
				final Item item = model.getItem(itemID);

				if(fields.get(BAR_PUBLISH_NOW)!=null)
				{
					for(final History history : History.getHistories(item.getCopeType()))
					{
						final History.Event event = history.createEvent(item, anchor.getHistoryAuthor(), false);
						event.createFeature(
								feature, feature.getName(),
								feature.isNull(item) ? null : ("file type=" + feature.getContentType(item) + " size=" + feature.getLength(item)),
								"file name=" + file.getName() + " type=" + file.getContentType() + " size=" + file.getSize());
					}
					
					// TODO use more efficient setter with File or byte[]
					feature.set(item, file.getInputStream(), file.getContentType());
				}
				else
				{
					anchor.modify(file, feature, item);
				}
				
				model.commit();
			}
			catch(NoSuchIDException e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
				model.rollbackIfNotCommitted();
			}
			
			referer = fields.get(BAR_REFERER);
		}
		else // isMultipartContent
		{
			if(request.getParameter(BAR_BORDERS_ON)!=null || request.getParameter(BAR_BORDERS_ON_IMAGE)!=null)
			{
				anchor.borders = true;
			}
			else if(request.getParameter(BAR_BORDERS_OFF)!=null || request.getParameter(BAR_BORDERS_OFF_IMAGE)!=null)
			{
				anchor.borders = false;
			}
			else if(request.getParameter(BAR_CLOSE)!=null || request.getParameter(BAR_CLOSE_IMAGE)!=null)
			{
				httpSession.removeAttribute(ANCHOR);
			}
			else if(request.getParameter(BAR_SWITCH_TARGET)!=null)
			{
				anchor.setTarget(getTarget(request.getParameter(BAR_SWITCH_TARGET)));
			}
			else if(request.getParameter(BAR_SAVE_TARGET)!=null)
			{
				try
				{
					startTransaction("saveTarget");
					anchor.getTarget().save(anchor);
					model.commit();
				}
				finally
				{
					model.rollbackIfNotCommitted();
				}
				anchor.notifyPublishedAll();
			}
			else
			{
				final String featureID = request.getParameter(BAR_FEATURE);
				if(featureID==null)
					throw new NullPointerException();
				
				final Feature featureO = model.getFeature(featureID);
				if(featureO==null)
					throw new NullPointerException(featureID);
				
				final String itemID = request.getParameter(BAR_ITEM);
				if(itemID==null)
					throw new NullPointerException();
				
				if(featureO instanceof StringField)
				{
					final StringField feature = (StringField)featureO;
					final String value = request.getParameter(BAR_TEXT);
				
					try
					{
						startTransaction("barText(" + featureID + ',' + itemID + ')');
						
						final Item item = model.getItem(itemID);
	
						if(request.getParameter(BAR_PUBLISH_NOW)!=null)
						{
							String v = value;
							if("".equals(v))
								v = null;
							for(final History history : History.getHistories(item.getCopeType()))
							{
								final History.Event event = history.createEvent(item, anchor.getHistoryAuthor(), false);
								event.createFeature(feature, feature.getName(), feature.get(item), v);
							}
							feature.set(item, v);
							anchor.notifyPublished(feature, item);
						}
						else
						{
							anchor.modify(value, feature, item);
						}
						
						model.commit();
					}
					catch(NoSuchIDException e)
					{
						throw new RuntimeException(e);
					}
					finally
					{
						model.rollbackIfNotCommitted();
					}
				}
				else
				{
					final IntegerField feature = (IntegerField)featureO;
					final String itemIDFrom = request.getParameter(BAR_ITEM_FROM);
					if(itemIDFrom==null)
						throw new NullPointerException();
					
					try
					{
						startTransaction("swapPosition(" + featureID + ',' + itemIDFrom + ',' + itemID + ')');
						
						final Item itemFrom = model.getItem(itemIDFrom);
						final Item itemTo   = model.getItem(itemID);
	
						final Integer positionFrom = feature.get(itemFrom);
						final Integer positionTo   = feature.get(itemTo);
						feature.set(itemFrom, feature.getMinimum());
						feature.set(itemTo,   positionFrom);
						feature.set(itemFrom, positionTo);
						
						for(final History history : History.getHistories(itemFrom.getCopeType()))
						{
							final History.Event event = history.createEvent(itemFrom, anchor.getHistoryAuthor(), false);
							event.createFeature(feature, feature.getName(), positionFrom, positionTo);
						}
						for(final History history : History.getHistories(itemTo.getCopeType()))
						{
							final History.Event event = history.createEvent(itemTo, anchor.getHistoryAuthor(), false);
							event.createFeature(feature, feature.getName(), positionTo, positionFrom);
						}
						
						model.commit();
					}
					catch(NoSuchIDException e)
					{
						throw new RuntimeException(e);
					}
					finally
					{
						model.rollbackIfNotCommitted();
					}
				}
			}
			
			referer = request.getParameter(BAR_REFERER);
		}
		
		if(referer!=null)
			response.sendRedirect(response.encodeRedirectURL(referer));
	}
	
	static final String MEDIA_FEATURE = "mf";
	static final String MEDIA_ITEM = "mi";
	
	private final void doMedia(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final Anchor anchor)
	{
		final String featureID = request.getParameter(MEDIA_FEATURE);
		if(featureID==null)
			throw new NullPointerException();
		final Media feature = (Media)model.getFeature(featureID);
		if(feature==null)
			throw new NullPointerException(featureID);
		
		final String itemID = request.getParameter(MEDIA_ITEM);
		if(itemID==null)
			throw new NullPointerException();
		
		final Item item;
		try
		{
			startTransaction("media(" + featureID + ',' + itemID + ')');
			item = model.getItem(itemID);
			model.commit();
		}
		catch(NoSuchIDException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			model.rollbackIfNotCommitted();
		}
		
		final FileItem fi = anchor.getModification(feature, item);
		if(fi==null)
			throw new NullPointerException(featureID + '-' + itemID);
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.addHeader("Cache-Control", "max-age=0");
		response.addHeader("Cache-Control", "must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", System.currentTimeMillis());
		response.setContentType(fi.getContentType());
		response.setContentLength((int)fi.getSize());
		
		InputStream in = null;
		ServletOutputStream out = null;
		try
		{
			in  = fi.getInputStream();
			out = response.getOutputStream();
			
			final byte[] b = new byte[20*1024];
			for(int len = in.read(b); len>=0; len = in.read(b))
				out.write(b, 0, len);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			if(in!=null)
			{
				try
				{
					in.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(out!=null)
			{
				try
				{
					out.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	static final String LOGIN_SUBMIT   = "login.submit";
	static final String LOGIN_USER     = "login.user";
	static final String LOGIN_PASSWORD = "login.password";
	
	private final void doLogin(
			final HttpServletRequest request,
			final HttpSession httpSession,
			final HttpServletResponse response)
	throws IOException
	{
		assert httpSession!=null;
		response.setContentType("text/html; charset="+UTF8);
		if(Cop.isPost(request) && request.getParameter(LOGIN_SUBMIT)!=null)
		{
			final String user = request.getParameter(LOGIN_USER);
			final String password = request.getParameter(LOGIN_PASSWORD);
			try
			{
				startTransaction("login");
				final Session session = login(user, password);
				if(session!=null)
				{
					httpSession.setAttribute(ANCHOR, new Anchor(defaultTarget, draftsEnabled, this, request, response, user, session, session.getName()));
					redirectHome(request, response);
				}
				else
				{
					final StringBuilder out = new StringBuilder();
					Login_Jspm.write(out, request, response.encodeURL(request.getContextPath() + request.getServletPath()), Editor.class.getPackage(), user);
					writeBody(out, response);
				}
				model.commit();
			}
			finally
			{
				model.rollbackIfNotCommitted();
			}
		}
		else
		{
			final StringBuilder out = new StringBuilder();
			Login_Jspm.write(out, request, response.encodeURL(request.getContextPath() + request.getServletPath()), Editor.class.getPackage(), null);
			writeBody(out, response);
		}
	}
	
	static final String ANCHOR = Session.class.getName();
	
	static final String EDIT_METHOD_LINE = AVOID_COLLISION + "line";
	static final String EDIT_METHOD_FILE = AVOID_COLLISION + "file";
	static final String EDIT_METHOD_AREA = AVOID_COLLISION + "area";
	
	private static final void writeBody(
			final StringBuilder out,
			final HttpServletResponse response)
		throws IOException
	{
		ServletOutputStream outStream = null;
		try
		{
			outStream = response.getOutputStream();
			final byte[] outBytes = out.toString().getBytes(UTF8);
			outStream.write(outBytes);
		}
		finally
		{
			if(outStream!=null)
				outStream.close();
		}
	}
}
