package com.nfl.dm.clubsites.cms.authors.main.tree;

import com.nfl.dm.clubsites.cms.authors.AuthorsNodeTypes;
import info.magnolia.ui.workbench.tree.drop.BaseDropConstraint;

/**
 * Authors App specific implementation of {@link info.magnolia.ui.workbench.tree.drop.DropConstraint}.
 */
public class AuthorDropConstraint extends BaseDropConstraint {

    public AuthorDropConstraint() {
        super(AuthorsNodeTypes.Author.NAME);
    }
}
