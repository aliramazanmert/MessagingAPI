package com.alirmert.offlinemessaging.block;

import com.alirmert.offlinemessaging.userlog.UserLog;
import com.alirmert.offlinemessaging.userlog.UserLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("blocks")
public class BlockController {
    private BlockRepository blockRepository;
    private UserLogRepository userLogRepository;

    public BlockController(BlockRepository blockRepository,UserLogRepository userLogRepository){
        this.blockRepository = blockRepository;
        this.userLogRepository = userLogRepository;
    }

    @GetMapping
    public List<Block> listBlockedUsers(){
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return blockRepository.findAllByBlockerUsername(loggedUser);
    }

    @PostMapping
    public UserLog blockUser(@RequestBody Block block){
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        block.setBlockerUsername(loggedUser);
        blockRepository.save(block);
        userLogRepository.save(new UserLog("User: " + loggedUser + " has blocked User: " + block.getBlockedUsername() ));
        return new UserLog("User is blocked successfully!");
    }

    @Transactional
    @DeleteMapping
    public UserLog unblockUser(@RequestParam String unblockedUser){
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        blockRepository.deleteByBlockerUsernameAndBlockedUsername(loggedUser,unblockedUser);
        userLogRepository.save(new UserLog("User: " + loggedUser + " has unblocked User: " + unblockedUser ));
        return new UserLog("User is unblocked successfully!");
    }
}
