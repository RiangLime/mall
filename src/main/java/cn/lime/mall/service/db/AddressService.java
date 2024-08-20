package cn.lime.mall.service.db;

import cn.lime.mall.model.entity.Address;
import cn.lime.mall.model.vo.AddressVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author riang
* @description 针对表【Address】的数据库操作Service
* @createDate 2024-07-31 15:03:53
*/
public interface AddressService extends IService<Address> {
    void addAddress(Long userId,String receiverName,String receiverAddress,String receiverPhone,Integer isDefault);
    void deleteAddress(Integer addressId);
    boolean updateAddress(Integer addressId,String receiverName,String receiverAddress,String receiverPhone,Integer isDefault);
    List<AddressVo> listUserAddress(Long userId);
}
