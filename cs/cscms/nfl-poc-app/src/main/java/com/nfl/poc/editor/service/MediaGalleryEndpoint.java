package com.nfl.poc.editor.service;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import info.magnolia.cms.util.ExclusiveWrite;
import info.magnolia.context.MgnlContext;
import info.magnolia.rest.AbstractEndpoint;
import info.magnolia.rest.service.node.definition.NodeEndpointDefinition;
import info.magnolia.rest.service.node.v1.RepositoryMarshaller;
import info.magnolia.rest.service.node.v1.RepositoryNode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Endpoint for accessing and manipulating media gallery nodes.
 *
 * @param <D> The endpoint definition
 */
@Api(value = "/media/v1", description = "The mediagallery API")
@Path("/media/v1")
public class MediaGalleryEndpoint<D extends NodeEndpointDefinition> extends AbstractEndpoint<D> {

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_NODE_ALREADY_EXISTS = "Node already exists";
    private static final String STATUS_MESSAGE_UNAUTHORIZED = "Unauthorized";
    private static final String STATUS_MESSAGE_ACCESS_DENIED = "Access denied";
    private static final String STATUS_MESSAGE_NODE_NOT_FOUND = "Node not found";
    private static final String STATUS_MESSAGE_ERROR_OCCURRED = "Error occurred";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private RepositoryMarshaller marshaller = new RepositoryMarshaller();

    @Inject
    public MediaGalleryEndpoint(final D endpointDefinition) {
        super(endpointDefinition);
    }

    /**
     * Creates a new node and populates it with the supplied properties. Does not support adding sub nodes. The
     * submitted node must contain name and type.
     */
    @PUT
    @Path("/{path:(.)*}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(value = "Create a node", notes = "Creates a node and adds passed properties")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 400, message = STATUS_MESSAGE_NODE_ALREADY_EXISTS),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 403, message = STATUS_MESSAGE_ACCESS_DENIED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Response createNode(
            @PathParam("path") String parentPath,
            RepositoryNode repositoryNode) throws RepositoryException {

        final String parentAbsPath = "/" + parentPath;

        if (StringUtils.isEmpty(repositoryNode.getName())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (StringUtils.isEmpty(repositoryNode.getType())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (!StringUtils.isEmpty(repositoryNode.getPath()) && !repositoryNode.getPath().equals(parentAbsPath + "/" + repositoryNode.getName())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (repositoryNode.getNodes() != null && !repositoryNode.getNodes().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Session session = MgnlContext.getJCRSession("mediagallery");

        if (!session.nodeExists(parentAbsPath)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Node parentNode = session.getNode(parentAbsPath);

        if (parentNode.hasNode(repositoryNode.getName())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Node node = parentNode.addNode(repositoryNode.getName(), repositoryNode.getType());

        if (repositoryNode.getProperties() != null) {
            marshaller.unmarshallProperties(node, repositoryNode.getProperties());
        }

        synchronized (ExclusiveWrite.getInstance()) {
            session.save();
        }

        log.debug("Created a new node [{}]", node.getPath());

        return Response.ok().build();
    }
}
