package cn.lime.mall.service.db.impl;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.YesNoEnum;
import cn.lime.mall.model.vo.AddressVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Address;
import cn.lime.mall.service.db.AddressService;
import cn.lime.mall.mapper.AddressMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author riang
 * @description 针对表【Address】的数据库操作Service实现
 * @createDate 2024-07-31 15:03:53
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address>
        implements AddressService {
    @Override
    @Transactional
    public void addAddress(Long userId, String receiverName,String receiverPosition, String receiverAddress, String receiverPhone, Integer isDefault) {
        Address add = new Address();
        add.setUserId(userId);
        add.setReceiverName(receiverName);
        add.setReceiverAddress(receiverAddress);
        add.setReceiverPhone(receiverPhone);
        add.setIsDefault(isDefault);
        add.setReceiverPosition(receiverPosition);
        if (ObjectUtils.isNotEmpty(isDefault) && isDefault == 1) {
            ThrowUtils.throwIf(!clearDefaultAddress(add.getUserId()),ErrorCode.UPDATE_ERROR);
        }
        ThrowUtils.throwIf(!save(add),ErrorCode.UPDATE_ERROR);
    }

    @Override
    public void deleteAddress(Integer addressId) {
        ThrowUtils.throwIf(!lambdaUpdate().eq(Address::getAddressId, addressId).remove(),ErrorCode.DELETE_ERROR);
    }

    @Override
    public boolean updateAddress(Integer addressId, String receiverName,String receiverPosition, String receiverAddress, String receiverPhone, Integer isDefault) {
        if (StringUtils.isEmpty(receiverName) && StringUtils.isEmpty(receiverAddress) &&
                StringUtils.isEmpty(receiverPhone) && ObjectUtils.isEmpty(isDefault)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更新项不可全空");
        }
        Address add = getById(addressId);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(add),ErrorCode.NOT_FOUND_ERROR);
        if (StringUtils.isNotEmpty(receiverName)) {
            add.setReceiverName(receiverName);
        }
        if (StringUtils.isNotEmpty(receiverAddress)) {
            add.setReceiverAddress(receiverAddress);
        }
        if (StringUtils.isNotEmpty(receiverPhone)) {
            add.setReceiverPhone(receiverPhone);
        }
        if (StringUtils.isNotEmpty(receiverPosition)){
            add.setReceiverPosition(receiverPosition);
        }
        if (ObjectUtils.isNotEmpty(isDefault)) {
            add.setIsDefault(isDefault);
            if (isDefault==1){
                ThrowUtils.throwIf(!clearDefaultAddress(add.getUserId()),ErrorCode.UPDATE_ERROR);
            }
        }
        return updateById(add);
    }

    @Override
    public List<AddressVo> listUserAddress(Long userId) {
        return lambdaQuery().eq(Address::getUserId,userId).list().stream().map(AddressVo::fromBean).toList();
    }

    public boolean clearDefaultAddress(Long userId){
        Optional<Address> defaultAdd = lambdaQuery().eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, YesNoEnum.YES.getVal()).oneOpt();
        return defaultAdd.map(address -> lambdaUpdate().eq(Address::getAddressId, address.getAddressId())
                .set(Address::getIsDefault, YesNoEnum.NO.getVal()).update()).orElse(true);
    }
}




