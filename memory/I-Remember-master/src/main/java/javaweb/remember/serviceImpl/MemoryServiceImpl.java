package javaweb.remember.serviceImpl;

import javaweb.remember.entity.Memory;
import javaweb.remember.repository.MemoryRepository;
import javaweb.remember.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryServiceImpl implements MemoryService {

    @Autowired
    private MemoryRepository memoryRepository;

    @Override
    public Memory save(Memory memory) {
        return memoryRepository.save(memory);
    }

    @Override
    public Memory findById(Long ID) {
        if(memoryRepository.findById(ID).isPresent()){
            return memoryRepository.findById(ID).get();
        }
        else{
            return null;
        }
    }

    @Override
    public List<Memory> findAllByCreator(Long id) {
        return memoryRepository.findAllByCreator(id);
    }

    // 删除记忆
    @Override
    public boolean deleteMemoryByID(Long id) {
        try {
            memoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    ;

    @Override
    public Memory randomMemory() {
        return memoryRepository.randomMemory();
    }

    @Override
    public List<Memory> searchMemory(String searchStr) {
        return memoryRepository.searchMemory(searchStr);
    }

    @Override
    public List<Memory> findAll(){return memoryRepository.findAll();}
}
