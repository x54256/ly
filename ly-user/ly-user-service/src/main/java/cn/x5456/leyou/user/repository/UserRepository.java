package cn.x5456.leyou.user.repository;

import cn.x5456.leyou.user.entity.TbUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TbUserEntity,Long> {

    TbUserEntity findByUsername(String username);

    TbUserEntity findByPhone(String phone);


}
