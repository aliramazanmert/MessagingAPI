package com.alirmert.offlinemessaging.message;

import com.alirmert.offlinemessaging.block.Block;
import com.alirmert.offlinemessaging.block.BlockRepository;
import com.alirmert.offlinemessaging.userlog.UserLog;
import com.alirmert.offlinemessaging.userlog.UserLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/messages")
public class MessageController {
    private MessageRepository messageRepository;
    private BlockRepository blockRepository;
    private UserLogRepository userLogRepository;

    public MessageController(MessageRepository messageRepository, BlockRepository blockRepository,UserLogRepository userLogRepository){
        this.messageRepository = messageRepository;
        this.blockRepository = blockRepository;
        this.userLogRepository = userLogRepository;
    }

    @GetMapping
    public List<Message> receiveMessages(){
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return messageRepository.findAllByToUser(loggedUser);
    }

    @PostMapping
    public UserLog sendMessage(@RequestBody Message message){
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(blockRepository.findByBlockerUsernameAndBlockedUsername(message.getToUser(),loggedUser) != null){
            //Cant send messages to that user since user is blocked
            userLogRepository.save(new UserLog("Message is not sent - User: " + loggedUser + " is blocked by User: " + message.getToUser() ));
            return new UserLog("Error: This user has blocked you!");
        }
        else{
            message.setFromUser(loggedUser);
            messageRepository.save(message);
            userLogRepository.save(new UserLog("Message is sent successfully - Sender User: " + loggedUser + " - Receiver User: " + message.getToUser() ));
            return new UserLog("Message sent!");
        }

    }
}
