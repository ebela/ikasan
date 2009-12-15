package org.ikasan.tools.messaging.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.ikasan.tools.messaging.destination.DestinationHandle;
import org.ikasan.tools.messaging.model.MapMessageWrapper;
import org.ikasan.tools.messaging.model.MessageWrapper;
import org.ikasan.tools.messaging.model.TextMessageWrapper;
import org.ikasan.tools.messaging.server.DestinationServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessagePublicationController {

	private DestinationServer destinationServer;
	
	public static final String DESTINATION_PATH_PARAMETER_NAME = "destinationPath";
	
	public static final String FILE_SYSTEM_PATH_PARAMETER_NAME = "fileSystemPath";
	
	public static final String MESSAGE_TEXT_PARAMETER_NAME = "messageText";
	
	public static final String MESSAGE_ID_PARAMETER_NAME = "messageId";

    public static final String MESSAGE_PRIORITY_PARAMETER_NAME = "priority";
    
    public static final String REPOSITORY_NAME_PARAMETER_NAME = "repositoryName";
    
    public static final String SUBSCRIPTION_NAME_PARAMETER_NAME = "subscriptionName";
    
    public static final String SIMPLE_SUBSCRIPTION_PARAMETER_NAME = "simpleSubscription";
    
    
    private Logger logger = Logger.getLogger(MessagePublicationController.class);
    
    private ServletFileUpload upload;

    @Autowired
	public MessagePublicationController(
			DestinationServer destinationServer) {
		super();
		this.destinationServer = destinationServer;
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		upload = new ServletFileUpload(factory);

	}
	

    @RequestMapping(value="/publishTextMessage.htm", method = RequestMethod.POST)
    public String publishTextMessage(@RequestParam(DESTINATION_PATH_PARAMETER_NAME) String destinationPath,
            @RequestParam(MESSAGE_TEXT_PARAMETER_NAME) String messageText, 
            @RequestParam(MESSAGE_PRIORITY_PARAMETER_NAME) int priority, ModelMap model)
    {	
    	destinationServer.publishTextMessage(destinationPath, messageText, priority);
    	return "redirect:/destination.htm?"+DESTINATION_PATH_PARAMETER_NAME+"="+destinationPath;
    }
    

    
    @RequestMapping("/destinations.htm")
    public String showDestinations(ModelMap model) 
    {	

    	model.addAttribute("destinations", destinationServer.getDestinations());
    	
        return "destinations";
    }
    
    @RequestMapping(value="/destination.htm", method = RequestMethod.GET)
    public String viewDestination(@RequestParam(DESTINATION_PATH_PARAMETER_NAME) String destinationPath,
             ModelMap model)
    {	
    	DestinationHandle   destination = destinationServer.getDestination(destinationPath);
    	model.addAttribute("destination", destination);
    	model.addAttribute("repositoryNames", order(destinationServer.getRepositories().keySet()));
    	model.addAttribute("subscriptionNames", order(destination.getSubscriptions().keySet()));
    	return "destination";
    }
    
    
    private List<String> order(Set<String> keySet) {
		List<String> orderedList = new ArrayList<String>(keySet);
		Collections.sort(orderedList);
		logger.info(orderedList);
		return orderedList;
	}


   
    
    
    @RequestMapping(value="/startSubscription.htm", method = RequestMethod.POST)
    public String startSubscriber(
    		@RequestParam(DESTINATION_PATH_PARAMETER_NAME) String destinationPath,
    		@RequestParam(value=REPOSITORY_NAME_PARAMETER_NAME,required=false) String repositoryName,
    		@RequestParam(SUBSCRIPTION_NAME_PARAMETER_NAME) String subscriptionName
    		)
    {	

    	destinationServer.createSubscription(subscriptionName, destinationPath, repositoryName);
    	
    	return "redirect:/destination.htm?"+DESTINATION_PATH_PARAMETER_NAME+"="+destinationPath;
    }
    
    @RequestMapping(value="/stopSubscription.htm", method = RequestMethod.POST)
    public String stopSubscriber(
    		@RequestParam(DESTINATION_PATH_PARAMETER_NAME) String destinationPath,
    		@RequestParam(SUBSCRIPTION_NAME_PARAMETER_NAME) String subscriptionName
    		)
    {	
    	destinationServer.destroyPersistingSubscription(destinationPath,subscriptionName);
    	
    	return "redirect:/destination.htm?"+DESTINATION_PATH_PARAMETER_NAME+"="+destinationPath;
    }
    
    @RequestMapping(value="/message.htm", method = RequestMethod.GET)
    public String viewMessage(
    		@RequestParam(DESTINATION_PATH_PARAMETER_NAME) String destinationPath,
    		@RequestParam(SUBSCRIPTION_NAME_PARAMETER_NAME) String subscriptionName,
    		@RequestParam(MESSAGE_ID_PARAMETER_NAME) String messageId,
             ModelMap model) throws JMSException
    {	
    	MessageWrapper message = destinationServer.getMessage(destinationPath,subscriptionName, messageId);
    	model.addAttribute("message", message);
    	
    	Map<String, String> messageProperties = new HashMap<String, String>();
//    	Enumeration propertyNames = message.getPropertyNames();
//    	while (propertyNames.hasMoreElements()){
//    		String propertyName = (String)propertyNames.nextElement();
//    		messageProperties.put(propertyName, message.getStringProperty(propertyName));
//    	}
//    	model.addAttribute("messageProperties", messageProperties);
    	
    	
    	
    	if (message instanceof TextMessageWrapper){
    		return "textMessage";
    	} else if (message instanceof MapMessageWrapper){
    		Map<String, Object> map = ((MapMessageWrapper)message).getMap();
    		
    		Map<String, Object> messageContent = new HashMap<String, Object>();
    		for (String mapKey : map.keySet()){
    			Object mapValue = map.get(mapKey);
    			String renderableValue=mapValue.toString();
    			if (mapValue instanceof byte[]){
    				renderableValue = "byte array comprising ["+new String((byte[])mapValue)+"]";
    			}
    			
				messageContent.put(mapKey, renderableValue);
    		}
    		model.addAttribute("messageContent", messageContent);
    		
			return "mapMessage";
		}
    	return "unsupportedMessage";
    	
    }
    
    
    @RequestMapping(value="/export.htm", method = RequestMethod.GET)
    public String downloadMessage(
    		@RequestParam(DESTINATION_PATH_PARAMETER_NAME) String destinationPath,
    		@RequestParam(SUBSCRIPTION_NAME_PARAMETER_NAME) String subscriptionName,
    		@RequestParam(MESSAGE_ID_PARAMETER_NAME) String messageId,
             ModelMap model, HttpServletResponse response) throws JMSException, IOException
    {	
    	MessageWrapper message = destinationServer.getMessage(destinationPath,subscriptionName, messageId);
    	String filename = message.getMessageId()+".xml";
    	
    	String  xmlString = destinationServer.getMessageAsXml(destinationPath,subscriptionName, messageId);
    	//response.setContentType("text/xml");
    	response.setContentType ("application/download");
		response.setHeader ("Content-Disposition", "attachment; filename=\""+filename+"\"");
        response.getOutputStream().write(xmlString.getBytes());
        
        return null;
    }
    
    @RequestMapping(value="/publishMapMessage.htm", method = RequestMethod.POST)
    public String publishMapMessage(@RequestParam(DESTINATION_PATH_PARAMETER_NAME) String destinationPath,
    		 //@RequestParam(MESSAGE_PRIORITY_PARAMETER_NAME) int priority,
    		 ModelMap model, HttpServletRequest request) throws JMSException, IOException, FileUploadException
    {	
    	logger.info("called");
    	
    	
    	int priority=4;
    	
    	
    	
    	
    	List<FileItem> fileItems = upload.parseRequest(request);
    	FileItem fileItem = fileItems.get(0);
    	
    	byte[] content = fileItem.get();
        
        String xml = new String(content, "UTF-8");
        

        
        destinationServer.publishXmlMessage(destinationPath, xml, priority);
    	return "redirect:/destination.htm?"+DESTINATION_PATH_PARAMETER_NAME+"="+destinationPath;
    }
   
    
    @RequestMapping("/repositories.htm")
    public String showRepositories(ModelMap model) 
    {	

    	model.addAttribute("repositories", destinationServer.getRepositories());
    	
        return "repositories";
    }
    
    @RequestMapping("/createFileSystemRepository.htm")
    public String createFileSystemRepository(
    		@RequestParam(REPOSITORY_NAME_PARAMETER_NAME)String name,  @RequestParam(FILE_SYSTEM_PATH_PARAMETER_NAME)String fileSystemPath, ModelMap map){
    	logger.info("called");
    	destinationServer.createFileSystemRepository(name, fileSystemPath);
    	return "redirect:/repositories.htm";
    }
    
}
