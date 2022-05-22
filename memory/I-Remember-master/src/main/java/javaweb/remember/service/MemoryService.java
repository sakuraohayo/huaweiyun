package javaweb.remember.service;

import javaweb.remember.entity.Memory;

import java.util.List;

public interface MemoryService {

    Memory save(Memory memory);

    Memory findById(Long ID);

    List<Memory> findAllByCreator(Long id);

    // 删除记忆
    boolean deleteMemoryByID(Long id);

    Memory randomMemory();

    List<Memory> searchMemory(String searchStr);

    List<Memory> findAll();
}
