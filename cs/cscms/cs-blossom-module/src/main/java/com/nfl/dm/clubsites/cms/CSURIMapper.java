package com.nfl.dm.clubsites.cms;

import info.magnolia.module.blossom.annotation.VirtualURIMapper;

import javax.servlet.http.HttpServletRequest;

/**
 * URIMapper that maps /about to /sections/about.
 */
@VirtualURIMapper
public class CSURIMapper {

    public String mapper(String uri, HttpServletRequest request) {
        if (uri.equals("/about")) {
            return "/sections/about";
        }
        return null;
    }
}
