package biz.aQute.resolve.internal.helpers;

import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.*;
import org.osgi.resource.*;
import org.osgi.service.repository.*;

import aQute.bnd.deployer.repository.*;

@Ignore
public class Utils {

    public static Repository createRepo(File index) {
        return createRepo(index, null);
    }

    public static Repository createRepo(File index, String name) {
        FixedIndexedRepo repo = new FixedIndexedRepo();

        Map<String,String> props = new HashMap<String,String>();
        props.put(FixedIndexedRepo.PROP_LOCATIONS, index.toURI().toString());
        if (name != null)
            props.put(AbstractIndexedRepo.PROP_NAME, name);
        repo.setProperties(props);

        return repo;
    }

    public static URI findContentURI(Resource resource) {
        List<Capability> contentCaps = resource.getCapabilities("osgi.content");
        if (contentCaps == null || contentCaps.isEmpty())
            throw new IllegalArgumentException("Resource has no content capability");
        if (contentCaps.size() > 1)
            throw new IllegalArgumentException("Resource has more than one content capability");

        Object uriObj = contentCaps.get(0).getAttributes().get("url");
        if (uriObj == null)
            throw new IllegalArgumentException("Resource content capability has no 'url' attribute.");
        if (uriObj instanceof URI)
            return (URI) uriObj;
        else
            try {
                return new URI(uriObj.toString());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Resource content capability has invalid 'url' attribute.", e);
            }
    }
}
