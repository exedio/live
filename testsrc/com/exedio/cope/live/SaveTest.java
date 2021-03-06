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

import static com.exedio.cope.junit.CopeAssert.assertContains;
import static com.exedio.cope.junit.CopeAssert.list;
import static junit.framework.Assert.assertEquals;

import com.exedio.cope.live.cope.CopeModel4Test;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;

@edu.umd.cs.findbugs.annotations.SuppressWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public class SaveTest extends CopeModel4Test
{
	public SaveTest()
	{
		super(DraftTest.MODEL);
	}

	static final Session SESSION = new Session()
	{
		@Override
		public String getName()
		{
			throw new RuntimeException();
		}
		@Override
		public String getHome(final HttpServletRequest request)
		{
			throw new RuntimeException();
		}
	};

	DraftedItem item;
	Draft draft;
	Anchor anchor;

	@Before
	@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
	public void setUp() throws Exception
	{
		new LiveServlet(DraftTest.MODEL)
		{
			private static final long serialVersionUID = 1l;
			@Override
			protected Session login(final String username, final String password)
			{
				throw new RuntimeException();
			}
		}.init();
		item = new DraftedItem();
		draft = new Draft("user", "name", "comment");
		item.setString("oldString1");
		anchor = new Anchor(true, new CopsDummyRequest(), "anchorUser", SESSION, "anchorSessionName");
		anchor.modify("newString1", DraftedItem.string, item);
	}

	@Test public void testDraft()
	{
		assertEquals(list(), draft.getItems());

		new TargetDraft(draft).save(anchor);
		assertEquals(1, draft.getItemsCount());
		final DraftItem di1 = draft.getItems().get(0);
		assertEquals("DraftedItem.string", di1.getFeature());
		assertEquals(item.getCopeID(), di1.getItem());
		assertEquals("oldString1", di1.getOldValue());
		assertEquals("newString1", di1.getNewValue());

		assertEquals("oldString1", item.getString());
		assertContains(draft, Draft.TYPE.search());
	}

	@Test public void testLive()
	{
		assertEquals("oldString1", item.getString());

		TargetLive.INSTANCE.save(anchor);
		assertEquals("newString1", item.getString());

		assertEquals(0, draft.getItemsCount());
		assertContains(draft, Draft.TYPE.search());
	}

	@Test public void testNewDraft()
	{
		assertContains(draft, Draft.TYPE.search());

		TargetNewDraft.INSTANCE.save(anchor);
		final Draft newDraft = Draft.TYPE.searchSingleton(Draft.TYPE.getThis().notEqual(draft));
		assertEquals("anchorSessionName", newDraft.getAuthor());
		assertEquals("new draft", newDraft.getComment());
		assertEquals("anchorSessionName - new draft", newDraft.getDropDownSummary());
		assertEquals(1, newDraft.getItemsCount());
		final DraftItem di1 = newDraft.getItems().get(0);
		assertEquals("DraftedItem.string", di1.getFeature());
		assertEquals(item.getCopeID(), di1.getItem());
		assertEquals("oldString1", di1.getOldValue());
		assertEquals("newString1", di1.getNewValue());


		assertContains(draft, newDraft, Draft.TYPE.search());
		assertEquals(0, draft.getItemsCount());
		assertEquals("oldString1", item.getString());

		DraftTest.MODEL.commit();
	}
}
