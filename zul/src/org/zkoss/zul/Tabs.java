/* Tabs.java

	Purpose:
		
	Description:
		
	History:
		Tue Jul 12 10:43:14     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import java.util.Iterator;

import org.zkoss.lang.Objects;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;

import org.zkoss.zul.Tabbox;
import org.zkoss.zul.impl.XulElement;

/**
 * A collection of tabs ({@link Tab}).
 *
 * <p>Default {@link #getZclass}: z-tabs. (since 3.5.2)
 * @author tomyeh
 */
public class Tabs extends XulElement implements org.zkoss.zul.api.Tabs {
	private String _align = "start";
	/** Returns the tabbox owns this component.
	 * <p>It is the same as {@link #getParent}.
	 */
	public Tabbox getTabbox() {
		return (Tabbox)getParent();
	}
	/** Returns the tabbox owns this component.
	 * <p>It is the same as {@link #getParent}.
	 * @since 3.5.2
	 */
	public org.zkoss.zul.api.Tabbox getTabboxApi() {
		return getTabbox();
	}
	public String getWidth() {
		String width = super.getWidth();
		Tabbox tabbox = getTabbox();
		if (width == null && tabbox != null && tabbox.isVertical())
			width = "50px";
		return width;
	}
	
	/** Returns the alignment of tab.
	 * Reserved for future extension; not supported yet.
	 * @since 3.0.0
	 */
	public String getAlign() {
		return _align;
	}
	/** Sets the alignment of tab.
	 * Reserved for future extension; not supported yet.
	 * <p>Default: "start".
	 * @param align must be "start" or "center" or "end".
	 * @since 3.0.0
	 */
	public void setAlign(String align) throws WrongValueException {
		if (!"start".equals(align) && !"center".equals(align) && !"end".equals(align))
			throw new WrongValueException(align);

		if (!Objects.equals(_align, align)) {
			_align = align;
			smartUpdate("align", _align);
		}
	}
	
	public void invalidate() {
		Tabbox tbox = getTabbox();
		if (tbox != null && tbox.isVertical())
			tbox.invalidate();
		else
			super.invalidate();
	}
	public String getZclass() {
		if (_zclass != null) return _zclass;
		final Tabbox tabbox = getTabbox();
		final String added = tabbox != null && tabbox.isVertical() ? "-ver" : ""; 
		return "z-tabs" + added;
	}
	//-- Component --//
	public void beforeParentChanged(Component parent) {
		if (parent != null && !(parent instanceof Tabbox))
			throw new UiException("Wrong parent: "+parent);
		super.beforeParentChanged(parent);
	}
	public void beforeChildAdded(Component child, Component refChild) {
		if (!(child instanceof Tab))
			throw new UiException("Unsupported child for tabs: "+child);
		super.beforeChildAdded(child, refChild);
	}
	public boolean insertBefore(Component child, Component refChild) {
		boolean sel = getChildren().isEmpty(), desel = false;
		final Tab newtab = (Tab)child;
		if (!sel && newtab.isSelected()) {
			newtab.setSelectedDirectly(false);	//turn off first
			sel = desel = true;					//trun on later
		}

		if (super.insertBefore(child, refChild)) {
			final Tabbox tabbox = getTabbox();

			if (sel)
				if (tabbox != null) {
					tabbox.setSelectedTab(newtab);
				} else {
					newtab.setSelectedDirectly(true);
					if (desel)
						for (Iterator it = getChildren().iterator(); it.hasNext();) {
							final Tab tab = (Tab)it.next();
							if (tab != newtab && tab.isSelected()) {
								tab.setSelectedDirectly(false);
								break;
							}
						}
				}
			return true;
		}
		return false;
	}
	public void onChildRemoved(Component child) {
		super.onChildRemoved(child);

		final Tabbox tabbox = getTabbox();
		if (tabbox != null)
			((Tab)child).removeEventListener(Events.ON_SELECT, tabbox._listener);
	}
	public void onChildAdded(Component child) {
		super.onChildAdded(child);

		final Tabbox tabbox = getTabbox();
		if (tabbox != null)
			((Tab)child).addEventListener(Events.ON_SELECT, tabbox._listener);
	}

	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		if (!"start".equals(_align))
			render(renderer, "align", _align);
	}
}
