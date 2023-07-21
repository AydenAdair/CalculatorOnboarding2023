package org.aydenadair.calculator;

import io.confluent.rest.Application;
import io.confluent.rest.entities.ErrorMessage;
import java.util.HashMap;
import javax.ws.rs.core.Configurable;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorApp extends Application<CalculatorConfig>{

    private static final Logger log = LoggerFactory.getLogger(CalculatorApp.class);

    public CalculatorApp(CalculatorConfig config) {
        super(config);
    }

    public static void main(String[] args) {
        try {
            HashMap<String, String> props = new HashMap<>();
            props.put("authentication.method", "BASIC");
            CalculatorApp app = new CalculatorApp(new CalculatorConfig(props));
            app.start();
            log.info("Server started and listening on port: " + app.server.getURI());
            app.join();
        } catch (Exception e) {
            log.error("Server failure: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void setupResources(final Configurable<?> configurable, final CalculatorConfig calculatorConfig) {
        configurable.register(new CalculatorResources());

        // IntelliJ suggests formatting it into a lambda expression, but this breaks its functionality
        ExceptionMapper<Exception> mapper = new ExceptionMapper<Exception>() {
            @Override
            public Response toResponse(final Exception e) {
                int badRequestStatusCode = Status.BAD_REQUEST.getStatusCode();
                return Response.status(badRequestStatusCode)
                        .entity(new ErrorMessage(badRequestStatusCode, e.getMessage()))
                        .build();
            }
        };

        configurable.register(mapper);
        configurable.register(new SwaggerResource());
    }

    @Override
    protected IdentityService createIdentityService() {
        return null;
    }

    @Override
    protected ConstraintMapping createGlobalAuthConstraint() {
        final Constraint constraint = new Constraint();
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"test"});

        final ConstraintMapping mapping = new ConstraintMapping();
        mapping.setConstraint(constraint);
        mapping.setMethod("*");
        // whatever path this is set to will require the basic authorization
        mapping.setPathSpec("/calc/audit");
        return mapping;
    }
    @Override
    protected LoginService createLoginService() {
        return new BasicLoginService();
    }

    private static class BasicLoginService extends AbstractLoginService {

        @Override
        protected String[] loadRoleInfo(final UserPrincipal userPrincipal) {
            if (userPrincipal.getName().equals("admin")) {
                return new String[]{"test"};
            }

            return new String[0];
        }

        @Override
        protected UserPrincipal loadUserInfo(final String name) {
            if (name.equals("admin")) {
                return new UserPrincipal(name, new Password("password"));
            }
            return null;
        }
    }
}
