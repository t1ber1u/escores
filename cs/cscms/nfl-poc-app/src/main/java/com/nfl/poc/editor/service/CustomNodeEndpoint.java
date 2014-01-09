package com.nfl.poc.editor.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.rest.AbstractEndpoint;
import info.magnolia.rest.service.node.definition.NodeEndpointDefinition;
import info.magnolia.rest.service.node.v1.RepositoryMarshaller;
import info.magnolia.rest.service.node.v1.RepositoryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Endpoint for accessing and manipulating nodes.
 *
 * @param <D> The endpoint definition
 */
@Api(value = "/repo/v1", description = "The repo API")
@Path("/repo/v1")
public class CustomNodeEndpoint<D extends NodeEndpointDefinition> extends AbstractEndpoint<D> {

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_UNAUTHORIZED = "Unauthorized";
    private static final String STATUS_MESSAGE_ACCESS_DENIED = "Access denied";
    private static final String STATUS_MESSAGE_NODE_NOT_FOUND = "Node not found";
    private static final String STATUS_MESSAGE_ERROR_OCCURRED = "Error occurred";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private RepositoryMarshaller marshaller = new RepositoryMarshaller();

    @Inject
    public CustomNodeEndpoint(final D endpointDefinition) {
        super(endpointDefinition);
    }

    /**
     * Returns a node including its properties and child nodes down to a certain depth.
     */
    @GET
    @Path("/{repository}/{contentType:(.)*}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get a node", notes = "Returns a node from the specified workspace and path")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 403, message = STATUS_MESSAGE_ACCESS_DENIED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Response readNode(
            @PathParam("repository") String repository,
            @PathParam("contentType") String contentType) throws RepositoryException {

        String query = "select * from [nt:base] where [mgnl:deleted] is null";
        NodeIterator articles = QueryUtil.search(repository, query, "JCR-SQL2", contentType);

        ArrayList<RepositoryNode> nodes = new ArrayList<RepositoryNode>();
        while (articles.hasNext()) {
            Node articleNode = articles.nextNode();
            RepositoryNode resNode = marshaller.marshallNode(articleNode);
            nodes.add(resNode);
        }

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(nodes);
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
    }

}
