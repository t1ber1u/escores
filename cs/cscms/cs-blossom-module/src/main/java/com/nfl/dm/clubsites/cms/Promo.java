package com.nfl.dm.clubsites.cms;

import info.magnolia.module.blossom.annotation.ComponentCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Component category annotation, all components annotated with it will be available in the promos area of
 * {@link CSTemplate}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ComponentCategory
public @interface Promo {
}
