package au.com.reecefenwick.api.apidocs;

import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.paths.Paths;

public class BasePathAwareRelativePathProvider extends AbstractPathProvider {
    private String basePath;

    public BasePathAwareRelativePathProvider(String basePath) {
        this.basePath = basePath;
    }

    @Override
    protected String applicationPath() {
        return basePath;
    }

    @Override
    protected String getDocumentationPath() {
        return "/";
    }

    @Override
    public String getOperationPath(String operationPath) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
        return Paths.removeAdjacentForwardSlashes(
                uriComponentsBuilder.path(operationPath.replaceFirst(basePath, "")).build().toString());
    }
}
