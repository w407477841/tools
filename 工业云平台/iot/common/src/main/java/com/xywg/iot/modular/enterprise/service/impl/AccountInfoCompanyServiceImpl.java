package com.xywg.iot.modular.enterprise.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.dto.EnterpriseCertificationDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.enterprise.dao.AccountInfoCompanyMapper;
import com.xywg.iot.modular.enterprise.model.*;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoCompanyVo;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoIndividualVo;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoReturnVo;
import com.xywg.iot.modular.enterprise.model.vo.FilePropertyVo;
import com.xywg.iot.modular.enterprise.service.*;
import com.xywg.iot.vo.ResultVO;
import com.xywg.iot.xycache.annotations.OpenCache;
import com.xywg.iot.xycache.annotations.RemoveCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 公司基本信息 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@Service
public class AccountInfoCompanyServiceImpl extends ServiceImpl<AccountInfoCompanyMapper, AccountInfoCompany> implements AccountInfoCompanyService {
    @Autowired
    private AccountInfoIndividualService accountInfoIndividualService;
    @Autowired
    private AccountInfoCompanyService accountInfoCompanyService;
    @Autowired
    private AccountInfoCompanyDescriptionService accountInfoCompanyDescriptionService;
    @Autowired
    private AccountInfoCompanyProductDescriptionService accountInfoCompanyProductDescriptionService;
    @Autowired
    private AccountInfoCompanyStarProductService accountInfoCompanyStarProductService;


    /**
     * 企业认证信息插入
     *
     * @param certificationDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResultVO insertEnterpriseCertification(EnterpriseCertificationDTO certificationDTO) {
        certificationDTO.setCreateTime(new Date());
        certificationDTO.setIsDel(0);

        //查询是否已经填写过申请人基本信息
        EntityWrapper<AccountInfoIndividual> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.eq("user_id", certificationDTO.getCreateUser());
        AccountInfoIndividual accountInfoIndividualDb = accountInfoIndividualService.selectOne(ew);
        if (accountInfoIndividualDb == null) {
            return new ResultVO(ResultCode.BASIC_INFORMATION_IS_NOT_FILLED_IN,  null);
        }

        //查询企业信息是否已经填写过了
        EntityWrapper<AccountInfoCompany> ewAccountInfoCompany = new EntityWrapper<>();
        ewAccountInfoCompany.eq("is_del", 0);
        ewAccountInfoCompany.eq("individual_id", accountInfoIndividualDb.getId());
        List<AccountInfoCompany> accountInfoCompanyList = accountInfoCompanyService.selectList(ewAccountInfoCompany);
        if (accountInfoCompanyList.size() > 0) {
            return new ResultVO(ResultCode.BASIC_ENTERPRISE_INFORMATION_ALREADY_EXISTS, null);
        }
        //企业信息
        AccountInfoCompany accountInfoCompany = new AccountInfoCompany();
        BeanUtils.copyProperties(certificationDTO, accountInfoCompany);
        accountInfoCompany.setIndividualId(accountInfoIndividualDb.getId());
        accountInfoCompany.setName(certificationDTO.getEnterpriseName());
        accountInfoCompanyService.insert(accountInfoCompany);

        //企业描述附件
        List<AccountInfoCompanyDescription> companyDescriptionList = new ArrayList<>();
        List<FilePropertyVo> companyDescriptionFileList = certificationDTO.getCompanyDescriptionFile();
        for (FilePropertyVo vo : companyDescriptionFileList) {
            AccountInfoCompanyDescription companyDescriptionFile = new AccountInfoCompanyDescription();
            BeanUtils.copyProperties(certificationDTO, companyDescriptionFile);
            companyDescriptionFile.setCompanyId(accountInfoCompany.getId());
            companyDescriptionFile.setAttachment(vo.getUrl());
            companyDescriptionFile.setName(vo.getName());
            companyDescriptionList.add(companyDescriptionFile);
        }
        if (companyDescriptionList.size() > 0) {
            accountInfoCompanyDescriptionService.insertBatch(companyDescriptionList);
        }

        //产品附件
        List<AccountInfoCompanyProductDescription> productDescriptionList = new ArrayList<>();
        List<FilePropertyVo> productDescriptionFileList = certificationDTO.getProductDescriptionFile();
        for (FilePropertyVo vo : productDescriptionFileList) {
            AccountInfoCompanyProductDescription productDescriptionFile = new AccountInfoCompanyProductDescription();
            BeanUtils.copyProperties(certificationDTO, productDescriptionFile);
            productDescriptionFile.setCompanyId(accountInfoCompany.getId());
            productDescriptionFile.setAttachment(vo.getUrl());
            productDescriptionFile.setName(vo.getName());
            productDescriptionList.add(productDescriptionFile);
        }
        if (productDescriptionList.size() > 0) {
            accountInfoCompanyProductDescriptionService.insertBatch(productDescriptionList);
        }

        //明星产品附件
        List<AccountInfoCompanyStarProduct> starProductList = new ArrayList<>();
        List<FilePropertyVo> starProductFileList = certificationDTO.getStarProductFile();
        for (FilePropertyVo vo : starProductFileList) {
            AccountInfoCompanyStarProduct companyStarProduct = new AccountInfoCompanyStarProduct();
            BeanUtils.copyProperties(certificationDTO, companyStarProduct);
            companyStarProduct.setAttachment(vo.getUrl());
            companyStarProduct.setName(vo.getName());
            companyStarProduct.setCompanyId(accountInfoCompany.getId());
            starProductList.add(companyStarProduct);
        }
        if (starProductList.size() > 0) {
            accountInfoCompanyStarProductService.insertBatch(starProductList);
        }

        //提交企业信息时 改变申请状态为1 申请中
        accountInfoIndividualDb.setAuditStatus(1);
        accountInfoIndividualService.updateById(accountInfoIndividualDb);

        return new ResultVO(ResultCode.SUCCESS, null);
    }

    /**
     * 填写的企业信息获取
     *
     * @param userId
     * @return
     */
    @Override
    public ResultVO getEnterpriseCertification(String userId) {
        //获取填写的申请人基础信息
        EntityWrapper<AccountInfoIndividualVo> infoIndividualEw = new EntityWrapper<>();
        infoIndividualEw.eq("a.user_id", userId);
        AccountInfoIndividualVo infoIndividualDb = accountInfoIndividualService.getAccountInfoIndividual(infoIndividualEw);

        //获取填写的企业信息
        AccountInfoCompanyVo infoCompanyDb = accountInfoCompanyService.getAccountInfoCompany(infoIndividualDb.getId());

        //获取企业描述附件信息
        EntityWrapper<AccountInfoCompanyDescription> ewCompanyDescription = new EntityWrapper<>();
        ewCompanyDescription.eq("company_id", infoCompanyDb.getId());
        ewCompanyDescription.eq("is_del", 0);
        List<AccountInfoCompanyDescription> companyDescriptionList = accountInfoCompanyDescriptionService.selectList(ewCompanyDescription);
        List<FilePropertyVo> companyDescriptionListUrl = new ArrayList<>();
        for (AccountInfoCompanyDescription accountInfoCompanyDescription : companyDescriptionList) {
            FilePropertyVo filePropertyVo= new  FilePropertyVo();
            filePropertyVo.setUrl( accountInfoCompanyDescription.getAttachment());
            filePropertyVo.setName(accountInfoCompanyDescription.getName());
            companyDescriptionListUrl.add(filePropertyVo);
        }
        //获取产品附件
        EntityWrapper<AccountInfoCompanyProductDescription> ewProductDescription = new EntityWrapper<>();
        ewProductDescription.eq("company_id", infoCompanyDb.getId());
        ewProductDescription.eq("is_del", 0);
        List<AccountInfoCompanyProductDescription> productDescriptionList = accountInfoCompanyProductDescriptionService.selectList(ewProductDescription);
        List<FilePropertyVo> productDescriptionListUrl = new ArrayList<>();
        for (AccountInfoCompanyProductDescription accountInfoCompanyProductDescription : productDescriptionList) {
            FilePropertyVo filePropertyVo= new  FilePropertyVo();
            filePropertyVo.setUrl( accountInfoCompanyProductDescription.getAttachment());
            filePropertyVo.setName(accountInfoCompanyProductDescription.getName());
            productDescriptionListUrl.add(filePropertyVo);
        }
        //获取明星产品
        EntityWrapper<AccountInfoCompanyStarProduct> ewStarProduct = new EntityWrapper<>();
        ewStarProduct.eq("company_id", infoCompanyDb.getId());
        ewStarProduct.eq("is_del", 0);
        List<AccountInfoCompanyStarProduct> starProductList = accountInfoCompanyStarProductService.selectList(ewStarProduct);
        List<FilePropertyVo> starProductListUrl = new ArrayList<>();
        for (AccountInfoCompanyStarProduct accountInfoCompanyStarProduct : starProductList) {
            FilePropertyVo filePropertyVo= new  FilePropertyVo();
            filePropertyVo.setUrl( accountInfoCompanyStarProduct.getAttachment());
            filePropertyVo.setName(accountInfoCompanyStarProduct.getName());
            starProductListUrl.add(filePropertyVo);
        }
        AccountInfoReturnVo accountInfoReturnVo = new AccountInfoReturnVo();
        accountInfoReturnVo.setCompany(infoCompanyDb);
        accountInfoReturnVo.setIndividual(infoIndividualDb);
        accountInfoReturnVo.setCompanyDescriptionFile(companyDescriptionListUrl);
        accountInfoReturnVo.setStarProductFile(starProductListUrl);
        accountInfoReturnVo.setProductDescriptionFile(productDescriptionListUrl);
        return new ResultVO(ResultCode.SUCCESS, accountInfoReturnVo);
    }

    /**
     * 企业信息修改
     *
     * @param certificationDTO
     * @return
     */
    @Override
    public ResultVO updateEnterpriseCertification(EnterpriseCertificationDTO certificationDTO) {
        certificationDTO.setModifyTime(new Date());
        //修改申请人申请状态
        AccountInfoIndividual infoIndividual = new AccountInfoIndividual();
        BeanUtils.copyProperties(certificationDTO, infoIndividual);
        infoIndividual.setName(certificationDTO.getIndividualName());
        infoIndividual.setAuditStatus(1);
        infoIndividual.setId(certificationDTO.getIndividualId());
        accountInfoIndividualService.updateById(infoIndividual);

        //修改企业基础信息
        AccountInfoCompany accountInfoCompany = new AccountInfoCompany();
        BeanUtils.copyProperties(certificationDTO, accountInfoCompany);
        accountInfoCompany.setName(certificationDTO.getEnterpriseName());
        accountInfoCompany.setId(certificationDTO.getEnterpriseId());
        accountInfoCompanyService.updateById(accountInfoCompany);

        accessoriesProcessing(certificationDTO);
        return new ResultVO(ResultCode.SUCCESS, null);
    }

    @Override
    public AccountInfoCompanyVo getAccountInfoCompany(Integer individualId) {
        return baseMapper.getAccountInfoCompany(individualId);
    }

    /**
     * 附件处理  此时,主表里的更新者是附件表里的创建者
     * <p>
     * 先删除原来的附件信息,再添加新的附件信息
     */
    private void accessoriesProcessing(EnterpriseCertificationDTO certificationDTO) {

        //1.处理企业描述附件
        EntityWrapper<AccountInfoCompanyDescription> ewCompanyDescription = new EntityWrapper<>();
        ewCompanyDescription.eq("company_id", certificationDTO.getEnterpriseId());
        ewCompanyDescription.eq("is_del", 0);
        //根据企业Id删除附件信息
        accountInfoCompanyDescriptionService.delete(ewCompanyDescription);

        //插入新的企业描述附件信息
        List<AccountInfoCompanyDescription> companyDescriptionList = new ArrayList<>();
        List<FilePropertyVo> companyDescriptionFileList = certificationDTO.getCompanyDescriptionFile();
        for (FilePropertyVo vo : companyDescriptionFileList) {
            AccountInfoCompanyDescription companyDescriptionFile = new AccountInfoCompanyDescription();
            companyDescriptionFile.setIsDel(0);
            companyDescriptionFile.setCompanyId(certificationDTO.getEnterpriseId());
            companyDescriptionFile.setAttachment(vo.getUrl());
            companyDescriptionFile.setName(vo.getName());
            companyDescriptionFile.setCreateTime(new Date());
            companyDescriptionFile.setCreateUser(certificationDTO.getModifyUser());
            companyDescriptionFile.setCreateUserName(certificationDTO.getModifyUserName());
            companyDescriptionList.add(companyDescriptionFile);
        }
        if (companyDescriptionList.size() > 0) {
            accountInfoCompanyDescriptionService.insertBatch(companyDescriptionList);
        }

        //2.处理产品附件
        //根据企业id先删除原来的附件信息
        EntityWrapper<AccountInfoCompanyProductDescription> ewCompanyProductDescription = new EntityWrapper<>();
        ewCompanyProductDescription.eq("company_id", certificationDTO.getEnterpriseId());
        ewCompanyProductDescription.eq("is_del", 0);
        //根据企业Id删除产品附件
        accountInfoCompanyProductDescriptionService.delete(ewCompanyProductDescription);
        List<AccountInfoCompanyProductDescription> productDescriptionList = new ArrayList<>();
        List<FilePropertyVo> productDescriptionFileList = certificationDTO.getProductDescriptionFile();
        for (FilePropertyVo vo : productDescriptionFileList) {
            AccountInfoCompanyProductDescription productDescriptionFile = new AccountInfoCompanyProductDescription();
            productDescriptionFile.setIsDel(0);
            productDescriptionFile.setCompanyId(certificationDTO.getEnterpriseId());
            productDescriptionFile.setAttachment(vo.getUrl());
            productDescriptionFile.setName(vo.getName());
            productDescriptionFile.setCreateTime(new Date());
            productDescriptionFile.setCreateUser(certificationDTO.getModifyUser());
            productDescriptionFile.setCreateUserName(certificationDTO.getModifyUserName());
            productDescriptionList.add(productDescriptionFile);
        }
        if (productDescriptionList.size() > 0) {
            accountInfoCompanyProductDescriptionService.insertBatch(productDescriptionList);
        }

        //3.明星产品附件
        //根据企业id先删除原来的附件信息
        EntityWrapper<AccountInfoCompanyStarProduct> ewStarProduct = new EntityWrapper<>();
        ewStarProduct.eq("company_id", certificationDTO.getEnterpriseId());
        ewStarProduct.eq("is_del", 0);
        accountInfoCompanyStarProductService.delete(ewStarProduct);
        List<AccountInfoCompanyStarProduct> starProductList = new ArrayList<>();
        List<FilePropertyVo> starProductFileList = certificationDTO.getStarProductFile();
        for (FilePropertyVo vo : starProductFileList) {
            AccountInfoCompanyStarProduct companyStarProduct = new AccountInfoCompanyStarProduct();
            companyStarProduct.setIsDel(0);
            companyStarProduct.setAttachment(vo.getUrl());
            companyStarProduct.setName(vo.getName());
            companyStarProduct.setCompanyId(certificationDTO.getEnterpriseId());
            companyStarProduct.setCreateTime(new Date());
            companyStarProduct.setCreateUser(certificationDTO.getModifyUser());
            companyStarProduct.setCreateUserName(certificationDTO.getModifyUserName());
            starProductList.add(companyStarProduct);
        }
        if (starProductList.size() > 0) {
            accountInfoCompanyStarProductService.insertBatch(starProductList);
        }
    }

    /**
     * 根据创建人查询 厂家
     * @param key
     * @return
     */
    @Override
    @OpenCache(exp = 500, model = "accountinfocompany", clazz = AccountInfoCompany.class)
    public AccountInfoCompany selectCacheOne(String key) {
        Wrapper<AccountInfoCompany> wrapper  =new EntityWrapper<>();
        wrapper.eq("create_user",key);
        return selectOne(wrapper);
    }

    @Override
    @RemoveCache( model = "accountinfocompany")
    public void removeCaches(String key) {

    }
}
