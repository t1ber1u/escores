package com.nfl.dm.clubsites.cms.articles;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import info.magnolia.ui.api.view.View;

/**
 * @author arash.shokoufandeh
 */
public class ArticleDetailSubAppViewImpl implements ArticleDetailSubAppView  {

    private ArticleDetailSubAppView.Listener listener;
    private View workbenchView;
    private Button previewButton;

    public ArticleDetailSubAppViewImpl() {
    }

    @Override
    public void setListener(ArticleDetailSubAppView.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void addPreviewButton(final String title) {
        Button button = new Button("Live Preview");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.previewArticle(title);
            }
        });
        this.previewButton = button;
    }

    @Override
    public void setContentView(View view) {
        this.workbenchView = view;
    }

    @Override
    public Component asVaadinComponent() {
        /*
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(workbenchView.asVaadinComponent());
        verticalLayout.addComponent(previewButton);
        */
        return workbenchView.asVaadinComponent();
    }

}
