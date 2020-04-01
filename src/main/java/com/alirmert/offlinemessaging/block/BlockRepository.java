package com.alirmert.offlinemessaging.block;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block,Long> {
    List<Block> findAllByBlockerUsername(String blockerUsername);
    void deleteByBlockerUsernameAndBlockedUsername(String blockerUsername, String blockedUsername);
    Block findByBlockerUsernameAndBlockedUsername(String blockerUsername,String blockedUsername);
}
