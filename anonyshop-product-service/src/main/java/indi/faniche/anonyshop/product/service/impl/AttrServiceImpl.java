package indi.faniche.anonyshop.product.service.impl;

/* File:   AttrServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   4/14/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.baseattr.PmsBaseAttrInfo;
import indi.faniche.anonyshop.bean.baseattr.PmsBaseAttrValue;
import indi.faniche.anonyshop.bean.baseattr.PmsBaseSaleAttr;
import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttr;
import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttrValue;
import indi.faniche.anonyshop.product.mapper.*;
import indi.faniche.anonyshop.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrMapper productSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper productSaleAttrValueMapper;

    /* 根据三级分类id查出该分类下的所有平台属性 */
    @Override
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id) {
        PmsBaseAttrInfo attrInfo = new PmsBaseAttrInfo();
        attrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> attrInfoList = baseAttrInfoMapper.select(attrInfo);

        for (PmsBaseAttrInfo item : attrInfoList) {
            PmsBaseAttrValue attrValue = new PmsBaseAttrValue();
            attrValue.setAttrId(item.getId());
            List<PmsBaseAttrValue> attrValueList = baseAttrValueMapper.select(attrValue);
            item.setAttrValueList(attrValueList);

            String attrValueLisStr = "";
            for (PmsBaseAttrValue attrValue1 : attrValueList) {
                attrValueLisStr += (attrValue1.getValueName() + " ");
            }
            item.setAttrValueListStr(attrValueLisStr);
        }
        return attrInfoList;
    }

    /* 删除平台销售属性 */
    @Override
    public void deleteAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        Example example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId", pmsBaseAttrInfo.getId());
        int result = baseAttrValueMapper.deleteByExample(example);
        int result2 = baseAttrInfoMapper.delete(pmsBaseAttrInfo);
    }

    /* 编辑平台销售属性 */
    @Override
    public void editAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        Example example = new Example(PmsBaseAttrInfo.class);
        example.createCriteria().andEqualTo("id", pmsBaseAttrInfo.getId());
        baseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo, example);
        baseAttrInfoMapper.updateByPrimaryKey(pmsBaseAttrInfo);

        // 删除旧的属性值
        example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId", pmsBaseAttrInfo.getId());
        baseAttrValueMapper.deleteByExample(example);
        // 将更改后的属性之插入
        insertOrUpdateAttrValue(pmsBaseAttrInfo);
    }

    /* 添加平台销售属性 */
    @Override
    public void addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        baseAttrInfoMapper.insert(pmsBaseAttrInfo);
        insertOrUpdateAttrValue(pmsBaseAttrInfo);
    }

    /* 获取基本销售属性 */
    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    public PmsProductSaleAttr addProdSaleAttr(PmsProductSaleAttr pmsProductSaleAttr) {
        productSaleAttrMapper.insert(pmsProductSaleAttr);
        insertOrUpdateSaleAttrValue(pmsProductSaleAttr);
        return pmsProductSaleAttr;
    }

    @Override
    public PmsProductSaleAttr deleteSaleAttr(PmsProductSaleAttr pmsProductSaleAttr) {
        // 删除销售属性值
        PmsProductSaleAttrValue saleAttrValue = new PmsProductSaleAttrValue();
        saleAttrValue.setProductId(pmsProductSaleAttr.getProductId());
        saleAttrValue.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
        productSaleAttrValueMapper.delete(saleAttrValue);
        pmsProductSaleAttr.setSpuSaleAttrValueList(null);
        pmsProductSaleAttr.setSpuSaleAttrValueListStr("");
        // 删除销售属性
        productSaleAttrMapper.delete(pmsProductSaleAttr);
        return pmsProductSaleAttr;
    }

    /* 更新基本平台属性值 */
    private void insertOrUpdateAttrValue(PmsBaseAttrInfo pmsBaseAttrInfo){
        String[] attrValueList = pmsBaseAttrInfo.getAttrValueListStr().split(" ");
        for (String attrValue : attrValueList) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValue.setValueName(attrValue);
            baseAttrValueMapper.insert(pmsBaseAttrValue);
        }
    }

    /* 更新商品  销售  属性值 */
    private void insertOrUpdateSaleAttrValue(PmsProductSaleAttr pmsProductSaleAttr){
        // 删除旧值
        PmsProductSaleAttrValue saleAttrValue = new PmsProductSaleAttrValue();
        saleAttrValue.setProductId(pmsProductSaleAttr.getProductId());
        saleAttrValue.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
        productSaleAttrValueMapper.delete(saleAttrValue);
        // 插入新指
        String[] attrValueList = pmsProductSaleAttr.getSpuSaleAttrValueListStr().split(" ");
        for (String attrValue : attrValueList) {
            PmsProductSaleAttrValue tmp = new PmsProductSaleAttrValue();
            tmp.setSaleAttrValueName(attrValue);
            tmp.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
            tmp.setProductId(pmsProductSaleAttr.getProductId());
            productSaleAttrValueMapper.insert(tmp);
        }
    }

}
