/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2016 jberry
 *
 *  This file author is jberry
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.styles.mindmapmode.styleeditorpanel;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.Icon;

import org.freeplane.api.ChildNodesLayout;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.resources.components.ButtonPanelProperty;
import org.freeplane.core.resources.components.ComboProperty;
import org.freeplane.core.resources.components.IPropertyControl;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.icon.factory.IconFactory;
import org.freeplane.features.layout.LayoutController;
import org.freeplane.features.layout.LayoutModel;
import org.freeplane.features.layout.mindmapmode.MLayoutController;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;

import com.jgoodies.forms.builder.DefaultFormBuilder;

/**
 * @author Joe Berry
 * Nov 27, 2016
 */
class ChildNodesLayoutControlGroup implements ControlGroup {
	static final String CHILD_NODES_LAYOUTS = "children_nodes_layouts";
	
	private static final ChildNodesLayout[] LAYOUTS = 
	        Arrays.asList(ChildNodesLayout.values()).stream().skip(1).toArray(ChildNodesLayout[]::new);
	private RevertingProperty mSetChildNodesLayout;
	private ButtonPanelProperty mChildNodesLayout;

	private ChildNodesLayoutChangeListener propertyChangeListener;

	private class ChildNodesLayoutChangeListener extends ControlGroupChangeListener {
		public ChildNodesLayoutChangeListener(final RevertingProperty mSet,final IPropertyControl... mProperty) {
			super(mSet, mProperty);
		}

		@Override
		void applyValue(final boolean enabled, final NodeModel node, final PropertyChangeEvent evt) {
			final MLayoutController styleController = (MLayoutController) Controller
					.getCurrentModeController().getExtension(LayoutController.class);
			styleController.setChildNodesLayout(node, enabled ? ChildNodesLayout.valueOf(mChildNodesLayout.getValue()) : null);
}

		@Override
		void setStyleOnExternalChange(NodeModel node) {
			LayoutModel model = LayoutModel.getModel(node);
			final ChildNodesLayout alignment = model != null ? model.getChildNodesLayout() : ChildNodesLayout.NOT_SET;
			ChildNodesLayout displayedValue = displayedValue(node, alignment);
			mChildNodesLayout.setValue(displayedValue.name());
		}

		private ChildNodesLayout displayedValue(NodeModel node, final ChildNodesLayout alignment) {
			final LayoutController styleController = LayoutController.getController();
			final ChildNodesLayout displayedValue = styleController.getChildNodesLayout(node);
			mSetChildNodesLayout.setValue(alignment != ChildNodesLayout.NOT_SET);
			return displayedValue;
		}

        @Override
        void adjustForStyle(NodeModel node) {
            StylePropertyAdjuster.adjustPropertyControl(node, mSetChildNodesLayout);
            StylePropertyAdjuster.adjustPropertyControl(node, mChildNodesLayout);
        }
	}
	
	public void addControlGroup(DefaultFormBuilder formBuilder) {
		mSetChildNodesLayout = new RevertingProperty();
		final Vector<Icon> icons = new Vector<>(LAYOUTS.length);
		ResourceController resourceController = ResourceController.getResourceController();
		for (int i = 0; i < LAYOUTS.length; i++) {
		    URL url = resourceController.getIconResource("/images/layouts/" + LAYOUTS[i].name().toLowerCase(Locale.ENGLISH) + ".svg?useAccentColor=true");
            icons.add(IconFactory.getInstance().getIcon(url, IconFactory.DEFAULT_UI_ICON_HEIGTH.zoomBy(1.5)));
		}
		Collection<String> alignmentNames = Stream.of(LAYOUTS).map(Enum::name).collect(Collectors.toList());
		mChildNodesLayout = new ButtonPanelProperty(CHILD_NODES_LAYOUTS, alignmentNames, icons);
		propertyChangeListener = new ChildNodesLayoutChangeListener(mSetChildNodesLayout, mChildNodesLayout);
		mSetChildNodesLayout.addPropertyChangeListener(propertyChangeListener);
		mChildNodesLayout.addPropertyChangeListener(propertyChangeListener);
		mChildNodesLayout.appendToForm(formBuilder);
		mSetChildNodesLayout.appendToForm(formBuilder);
	}
	
	public void setStyle(NodeModel node, boolean canEdit) {
		propertyChangeListener.setStyle(node);
	}

	
}