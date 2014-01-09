package com.nfl.dm.clubsites.cms.articles.main.tree;

import com.nfl.dm.clubsites.cms.articles.ArticlesNodeTypes;
import info.magnolia.ui.workbench.tree.drop.BaseDropConstraint;

/**
 * Articles App specific implementation of {@link info.magnolia.ui.workbench.tree.drop.DropConstraint}.
 */
public class ArticleDropConstraint extends BaseDropConstraint {

    public ArticleDropConstraint() {
        super(ArticlesNodeTypes.Article.NAME);
    }

}
