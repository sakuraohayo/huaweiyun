package javaweb.remember.repository;

import javaweb.remember.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory,Long> {

    @Modifying
    @Query(value = "delete from Memory memory where memory.createTime < ?1")
    void memoryDisappear(String date);

    @Query(value = "select * from Memory memory where memory.creator = ?1", nativeQuery = true)
    List<Memory> findAllByCreator(Long id);

    void deleteById(Long id);

    @Query(value = "select * from Memory order by rand() limit 1", nativeQuery = true)
    Memory randomMemory();

    @Query(value = "select * from Memory where tags like %?1% OR title like %?1% OR content like %?1%", nativeQuery = true)
    List<Memory> searchMemory(String searchStr);
}
