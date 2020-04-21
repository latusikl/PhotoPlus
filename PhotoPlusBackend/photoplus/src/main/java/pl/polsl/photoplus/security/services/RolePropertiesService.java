package pl.polsl.photoplus.security.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Stores properties for each Role.
 * Properties are read from xml file.
 * Example format for role is shown in comment at the beginning of the file.
 */
@Service
public class RolePropertiesService
{

    private final String PROP_FILE = "roleConfig.xml";

    private final String ROLE_TAG_NAME = "role";

    private final String URL_ACCESS_TAG_NAME = "access";

    private final String ROLE_ATTRIBUTE_NAME = "name";

    /**
     * Collection holds list of allowed URL's for given role name.
     */
    private Map<String,List<String>> roleAuthenticatorsMap;

    RolePropertiesService()
    {
        this.roleAuthenticatorsMap = new HashMap<>();
    }

    /**
     * Function is responsible for reading XML file and filling roleAuthenticatorMap object.
     *
     * @throws InputException When some error occurred during xml reading.
     */
    @PostConstruct
    public void loadRoleConfig() throws InputException
    {
        try {
            final InputStream roleInputStream = new ClassPathResource(PROP_FILE).getInputStream();
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //Create DOM for file
            final Document document = documentBuilder.parse(roleInputStream);
            final NodeList roleNodes = document.getElementsByTagName(ROLE_TAG_NAME);

            fillRoleAuthenticatorsMap(roleNodes);

        } catch (final ParserConfigurationException e) {
            throw new InputException("Unable to create document builder \n" + e.getStackTrace());
        } catch (final SAXException e) {
            throw new InputException("Unable to parse XML file \n" + e.getStackTrace());
        } catch (final IOException e) {
            throw new InputException("Unable to read document. \n" + e.getStackTrace());
        }
    }

    /**
     * Function fills roleAuthenticatorMap object.
     * Based on NodeList from XML tree.
     */
    private void fillRoleAuthenticatorsMap(final NodeList roleNodes)
    {
        for (int i = 0; i < roleNodes.getLength(); i++) {
            //Get first in line role tag
            final Node currentRoleNode = roleNodes.item(i);
            //Get its child's (access tags for this role)
            final NodeList roleAuthentications = currentRoleNode.getChildNodes();
            final List<String> roleAllowedUrls = new ArrayList<>();

            for (int j = 0; j < roleAuthentications.getLength(); j++) {
                final Node currentRoleAccessNode = roleAuthentications.item(j);
                //Bypass if tag name not valid
                if (currentRoleAccessNode.getNodeType() == Node.ELEMENT_NODE && currentRoleAccessNode.getNodeName()
                        .equals(URL_ACCESS_TAG_NAME)) {
                    roleAllowedUrls.add(currentRoleAccessNode.getTextContent());
                }
            }
            try {
                final NamedNodeMap nodeAttributesMap = currentRoleNode.getAttributes();
                final String roleName = nodeAttributesMap.getNamedItem(ROLE_ATTRIBUTE_NAME).getNodeValue();
                this.roleAuthenticatorsMap.put(roleName, roleAllowedUrls);
            } catch (final NullPointerException e) {
                System.out.println("WARNING! [RoleProperties.java|fillRoleAuthenticatorsMap] mistake in XML file. Role bypassed.");
            }
        }
    }

    public Set<String> getRoleNames()
    {
        return roleAuthenticatorsMap.keySet();
    }

    public List<String> getPropertiesByRoleName(final String roleName)
    {
        return this.roleAuthenticatorsMap.get(roleName);
    }

}
