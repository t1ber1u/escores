package com.nfl.dm.clubsites.cms.personnel.main.tree;

import com.nfl.dm.clubsites.cms.personnel.PersonnelNodeTypes;
import info.magnolia.ui.workbench.tree.drop.BaseDropConstraint;

/**
 * Authors App specific implementation of {@link info.magnolia.ui.workbench.tree.drop.DropConstraint}.
 */
public class PersonnelDropConstraint extends BaseDropConstraint {

    public PersonnelDropConstraint() {
        super(PersonnelNodeTypes.Personnel.NAME);
    }
}
