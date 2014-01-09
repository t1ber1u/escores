/**
 * This file Copyright (c) 2013 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging;

import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging.components.SearchEngineMetaDescription;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging.components.TagField;
import com.nfl.dm.clubsites.cms.articles.util.domain.Tag;
import com.vaadin.ui.*;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 25/12/13
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public class TaggingViewImpl implements TaggingView {

    private HorizontalLayout layout = new HorizontalLayout();

    private VerticalLayout tagLayout = new VerticalLayout();

    private SearchEngineMetaDescription metaDescription = new SearchEngineMetaDescription();

    private CssLayout customTagLayout = new FloatCssLayout();

    private CssLayout personsTagLayout = new FloatCssLayout();

    private CssLayout teamTagLayout = new FloatCssLayout();

    private CssLayout scheduleTagLayout = new FloatCssLayout();

    private Listener listener;

    public TaggingViewImpl() {
        construct();
    }

    private void construct() {
        buildTagLayout();
        buildSearchEngineMetaLayout();
        layout.setSizeFull();
        layout.addComponent(tagLayout);
        layout.addComponent(metaDescription);
        layout.setMargin(true);
    }

    private void buildSearchEngineMetaLayout() {
        buildCustomTagsLayout();
        buildRecommendedTagsLayout();
    }

    private void buildRecommendedTagsLayout() {
        VerticalLayout recommendedTagsLayout = new VerticalLayout();
        personsTagLayout.setCaption("PERSONS");
        teamTagLayout.setCaption("TEAM/GEOGRAPHY");
        scheduleTagLayout.setCaption("SCHEDULE");

        recommendedTagsLayout.setCaption("RECOMMENDED TAGS");
        recommendedTagsLayout.addComponent(personsTagLayout);
        recommendedTagsLayout.addComponent(teamTagLayout);
        recommendedTagsLayout.addComponent(scheduleTagLayout);

        tagLayout.addComponent(recommendedTagsLayout);
        tagLayout.setExpandRatio(recommendedTagsLayout, 1f);
    }

    private void buildCustomTagsLayout() {

        final VerticalLayout customTags = new VerticalLayout();
        customTags.setCaption("Custom Tags");

        final HorizontalLayout newTagLayout = new HorizontalLayout();
        newTagLayout.setSizeUndefined();

        final TextField newTagTextField = new TextField();
        newTagTextField.addStyleName("new-tag-textfield");
        newTagTextField.setHeight("52px");
        newTagTextField.setWidth("320px");
        newTagTextField.setNullRepresentation("");
        newTagTextField.setInputPrompt("Add new tag");
        NativeButton addTagButton = new NativeButton("", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addCustomTag(newTagTextField);
            }
        });
        addTagButton.addStyleName("add-tag-button");

        newTagLayout.addComponent(newTagTextField);
        newTagLayout.addComponent(addTagButton);
        newTagLayout.setExpandRatio(newTagTextField, 1f);

        /*
        NativeButton addGenerateTagsButton = new NativeButton("", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // Generate Tags
            }
        });
        addGenerateTagsButton.addStyleName("generate-tags-button");
        addGenerateTagsButton.setCaption("Generate Tags");
        newTagLayout.addComponent(addGenerateTagsButton);
        */

        customTags.addComponent(newTagLayout);
        customTags.addComponent(customTagLayout);

        this.tagLayout.addComponent(customTags);
    }

    private void addCustomTag(final TextField newTagTextField) {
        listener.onTagCreated(newTagTextField.getValue());
        newTagTextField.setValue(null);
    }

    private void buildTagLayout() {
        tagLayout.setSizeFull();
    }

    @Override
    public Component asVaadinComponent() {
        return layout;
    }

    @Override
    public void addPersonTag(final Tag tag) {
        addTagToLayout(personsTagLayout, tag);

    }

    private void addTagToLayout(CssLayout layout, final Tag tag) {
        layout.addComponent(new TagField(tag, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.onTagRemoved(tag);
            }
        }));
    }

    @Override
    public void addTeamTag(final Tag tag) {
        addTagToLayout(teamTagLayout, tag);
    }

    @Override
    public void addScheduleTag(Tag tag) {
        addTagToLayout(scheduleTagLayout, tag);
    }

    @Override
    public void addCustomTag(Tag tag) {
        addTagToLayout(customTagLayout, tag);
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private static class FloatCssLayout extends CssLayout {

        private FloatCssLayout() {
            setWidth("100%");
        }

        @Override
        protected String getCss(Component c) {
            return "float:left";
        }
    }

    @Override
    public void removeAll() {
        customTagLayout.removeAllComponents();
        teamTagLayout.removeAllComponents();
        scheduleTagLayout.removeAllComponents();
        personsTagLayout.removeAllComponents();
    }

    public void setArticleItem(JcrNodeAdapter articleItem) {
        metaDescription.setArticleItem(articleItem);
    }
}
