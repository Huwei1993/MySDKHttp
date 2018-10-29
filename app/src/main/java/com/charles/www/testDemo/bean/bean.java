package com.charles.www.testDemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 */

public class bean {
    private String codeValue;
    private String id;
    private String pageVersionName;
    private List<PackageListBean> packageList;

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageVersionName() {
        return pageVersionName;
    }

    public void setPageVersionName(String pageVersionName) {
        this.pageVersionName = pageVersionName;
    }

    public List<PackageListBean> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<PackageListBean> packageList) {
        this.packageList = packageList;
    }

    class PackageListBean{
        private String packageId;
        private String id;
        private String codeValue;
        private int shortNumber;
        private List<CmsItemsBean> componentList;

        public List<CmsItemsBean> getComponentList() {
            return componentList;
        }

        public void setComponentList(List<CmsItemsBean> componentList) {
            this.componentList = componentList;
        }

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public int getShortNumber() {
            return shortNumber;
        }

        public void setShortNumber(int shortNumber) {
            this.shortNumber = shortNumber;
        }


    }

    class CmsItemsBean implements Serializable {
        public CmsItemsBean(String salePrice, String title, String inStock, String subtitle, String description) {
            this.salePrice = salePrice;
            this.title = title;
            this.inStock = inStock;
            this.subtitle = subtitle;
            this.description = description;
        }

        public CmsItemsBean() {
        }

        private String videoPlayBackUrl;
        private String firstImgUrl;
        private int isNew;
        private String lgroup;
        private int videoStatus;
        private List<String> gifts;
        private String integral;
        private String salesVolume;
        private String originalPrice;
        private String salePrice;
        private String id;
        private String title;
        private long componentId;
        private String codeValue;
        private String contentCode;
        private String contentType;
        private String destinationUrl;
        private String graphicText;
        private String curruntDateLong;
        private String countryImgUrl; //国家url

        private String playDateLong;
        private String discount;
        private String inStock;
        private String subtitle;
        private String subTitle;
        private String watchNumber;

        private String author;
        private String videoDate;
        private String videoSeq;
        private String description;
        private List<String> labelName;
        private int isSoldOut;//0 有货 1 售罄
        private List<CmsItemsBean> componentList;

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getCountryImgUrl() {
            return countryImgUrl;
        }

        public void setCountryImgUrl(String countryImgUrl) {
            this.countryImgUrl = countryImgUrl;
        }


        public String getCurruntDateLong() {
            return curruntDateLong;
        }

        public void setCurruntDateLong(String curruntDateLong) {
            this.curruntDateLong = curruntDateLong;
        }

        public String getPlayDateLong() {
            return playDateLong;
        }

        public void setPlayDateLong(String playDateLong) {
            this.playDateLong = playDateLong;
        }


        public int getIsNew() {
            return isNew;
        }

        public void setIsNew(int isNew) {
            this.isNew = isNew;
        }

        public String getLgroup() {
            return lgroup;
        }

        public void setLgroup(String lgroup) {
            this.lgroup = lgroup;
        }

        public String getDestinationUrl() {
            return destinationUrl;
        }

        public void setDestinationUrl(String destinationUrl) {
            this.destinationUrl = destinationUrl;
        }

        public String getGraphicText() {
            return graphicText;
        }

        public void setGraphicText(String graphicText) {
            this.graphicText = graphicText;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getInStock() {
            return inStock;
        }

        public void setInStock(String inStock) {
            this.inStock = inStock;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getWatchNumber() {
            return watchNumber;
        }

        public void setWatchNumber(String watchNumber) {
            this.watchNumber = watchNumber;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getVideoDate() {
            return videoDate;
        }

        public void setVideoDate(String videoDate) {
            this.videoDate = videoDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getLabelName() {
            return labelName;
        }

        public void setLabelName(List<String> labelName) {
            this.labelName = labelName;
        }

        public String getVideoPlayBackUrl() {
            return videoPlayBackUrl;
        }

        public void setVideoPlayBackUrl(String videoPlayBackUrl) {
            this.videoPlayBackUrl = videoPlayBackUrl;
        }

        public String getFirstImgUrl() {
            return firstImgUrl;
        }

        public void setFirstImgUrl(String firstImgUrl) {
            this.firstImgUrl = firstImgUrl;
        }

        public List<String> getGifts() {
            return gifts;
        }

        public void setGifts(List<String> gifts) {
            this.gifts = gifts;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getSalesVolume() {
            return salesVolume;
        }

        public void setSalesVolume(String salesVolume) {
            this.salesVolume = salesVolume;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getComponentId() {
            return componentId;
        }

        public void setComponentId(long componentId) {
            this.componentId = componentId;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public String getContentCode() {
            return contentCode;
        }

        public void setContentCode(String contentCode) {
            this.contentCode = contentCode;
        }

        public List<CmsItemsBean> getComponentList() {
            return componentList;
        }

        public void setComponentList(List<CmsItemsBean> componentList) {
            this.componentList = componentList;
        }

        public int getVideoStatus() {
            return videoStatus;
        }

        public void setVideoStatus(int videoStatus) {
            this.videoStatus = videoStatus;
        }

        public int getIsSoldOut() {
            return isSoldOut;
        }

        public void setIsSoldOut(int isSoldOut) {
            this.isSoldOut = isSoldOut;
        }

        public String getVideoSeq() {
            return videoSeq;
        }

        public void setVideoSeq(String videoSeq) {
            this.videoSeq = videoSeq;
        }
    }


    @Override
    public String toString() {
        return "bean{" +
                "codeValue='" + codeValue + '\'' +
                ", id='" + id + '\'' +
                ", pageVersionName='" + pageVersionName + '\'' +
                ", packageList=" + packageList +
                '}';
    }
}
