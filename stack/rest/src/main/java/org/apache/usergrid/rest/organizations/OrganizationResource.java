/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.usergrid.rest.organizations;


import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.usergrid.rest.RootResource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.apache.usergrid.exception.NotImplementedException;
import org.apache.usergrid.management.OrganizationInfo;
import org.apache.usergrid.rest.AbstractContextResource;
import org.apache.usergrid.rest.applications.ApplicationResource;
import org.apache.usergrid.rest.exceptions.NoOpException;
import org.apache.usergrid.rest.exceptions.OrganizationApplicationNotFoundException;
import org.apache.usergrid.rest.security.annotations.RequireOrganizationAccess;
import org.apache.usergrid.rest.utils.PathingUtils;
import org.apache.usergrid.security.shiro.utils.SubjectUtils;

import org.apache.shiro.authz.UnauthorizedException;

import com.google.common.collect.BiMap;
import com.sun.jersey.api.json.JSONWithPadding;

import static org.apache.usergrid.persistence.cassandra.CassandraService.MANAGEMENT_APPLICATION_ID;


@Component("org.apache.usergrid.rest.organizations.OrganizationResource")
@Scope("prototype")
@Produces({
        MediaType.APPLICATION_JSON, "application/javascript", "application/x-javascript", "text/ecmascript",
        "application/ecmascript", "text/jscript"
})
public class OrganizationResource extends AbstractContextResource {

    String organizationName;


    public OrganizationResource() {

    }


    public OrganizationResource init( String organizationName ) {
        this.organizationName = organizationName;
        return this;
    }


    private ApplicationResource appResourceFor( UUID applicationId ) throws Exception {
        if ( applicationId.equals( MANAGEMENT_APPLICATION_ID ) && !SubjectUtils.isServiceAdmin() ) {
            throw new UnauthorizedException();
        }

        return getSubResource( ApplicationResource.class ).init( applicationId );
    }


    @Path(RootResource.APPLICATION_ID_PATH)
    public ApplicationResource getApplicationById( @PathParam("applicationId") String applicationIdStr )
            throws Exception {

        if ( "options".equalsIgnoreCase( request.getMethod() ) ) {
            throw new NoOpException();
        }

        UUID applicationId = UUID.fromString( applicationIdStr );
        if ( applicationId == null ) {
            return null;
        }

        OrganizationInfo org_info = management.getOrganizationByName( organizationName );
        UUID organizationId = null;
        if ( org_info != null ) {
            organizationId = org_info.getUuid();
        }
        if (organizationId == null) {
            return null;
        }
        BiMap<UUID, String> apps = management.getApplicationsForOrganization( organizationId );
        if ( apps.get( applicationId ) == null ) {
            return null;
        }

        return appResourceFor( applicationId );
    }


    @Path("applications/"+ RootResource.APPLICATION_ID_PATH)
    public ApplicationResource getApplicationById2( @PathParam("applicationId") String applicationId )
            throws Exception {
        return getApplicationById( applicationId );
    }


    @Path("apps/"+RootResource.APPLICATION_ID_PATH)
    public ApplicationResource getApplicationById3( @PathParam("applicationId") String applicationId )
            throws Exception {
        return getApplicationById( applicationId );
    }


    @Path("{applicationName}")
    public ApplicationResource getApplicationByName( @PathParam("applicationName") String applicationName )
            throws Exception {
        if ( "options".equalsIgnoreCase( request.getMethod() ) ) {
            throw new NoOpException();
        }
        if (!isSafe(applicationName)) {
            throw new IllegalArgumentException("Invalid application name");
        }
        String orgAppName = PathingUtils.assembleAppName( organizationName, applicationName );
        UUID applicationId = emf.lookupApplication( orgAppName );
        if ( applicationId == null ) {
            throw new OrganizationApplicationNotFoundException( orgAppName, uriInfo, properties );
        }

        return appResourceFor( applicationId );
    }


    @Path("applications/{applicationName}")
    public ApplicationResource getApplicationByName2( @PathParam("applicationName") String applicationName )
            throws Exception {
        return getApplicationByName( applicationName );
    }


    @Path("apps/{applicationName}")
    public ApplicationResource getApplicationByName3( @PathParam("applicationName") String applicationName )
            throws Exception {
        return getApplicationByName( applicationName );
    }


    @Path("a/{applicationName}")
    public ApplicationResource getApplicationByName4( @PathParam("applicationName") String applicationName )
            throws Exception {
        return getApplicationByName( applicationName );
    }


    @DELETE
    @RequireOrganizationAccess
    public JSONWithPadding executeDelete( @Context UriInfo ui,
                                          @QueryParam("callback") @DefaultValue("callback") String callback )
            throws Exception {


        throw new NotImplementedException( "Organization delete is not allowed yet" );
    }
}
