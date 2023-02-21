package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.entity.*;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

@Service
public class MyWardrobe {

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    @Resource
    private WardrobeClothesRepository wardrobeClothesRepository;

    @Resource
    private ClothesRepository clothesRepository;

    @Resource
    private LocationRepository locationRepository;

    @Resource
    private LocationHistoryRepository locationHistoryRepository;

    @Resource
    private OutfitRepository outfitRepository;

    private Wardrobe wardrobe;

    public void init(User user){
        if (user == null){
            throw new BaseException("the user doesn't exist");
        }

        if (wardrobeRepository.findByUser(user) == null){
            Wardrobe wardrobe = new Wardrobe(user);
            wardrobeRepository.save(wardrobe);
        }

        this.wardrobe = wardrobeRepository.findByUser(user);
    }

    /**
     * clothes相关
     */

    public void addClothes(Clothes clothes) {
        //imageUrl验证
        if (clothes.getImageUrl() == null || Objects.equals(clothes.getImageUrl(), "")){
            throw new BaseException("the image is empty");
        }

        //类别验证，若不存在则新建分类,若空则归为未分类
        String categoryName = clothes.getCategoryName();
        if (categoryName == null || categoryName.equals("")){
            categoryName = "未分类";
            clothes.setCategoryName(categoryName);
        }

        if (categoryRepository.findByWardrobeAndName(wardrobe, categoryName) == null){
            categoryRepository.save(new Category(wardrobe, categoryName));
        }

        if (locationRepository.findByWardrobeAndName(wardrobe, clothes.getLocation()) == null){
            locationRepository.save(new Location(wardrobe, clothes.getLocation()));
        }

        //向衣柜中添加衣物
        clothesRepository.save(clothes);
        WardrobeClothes wardrobeClothes = new WardrobeClothes(this.wardrobe,clothes);
        wardrobeClothesRepository.save(wardrobeClothes);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        df.setTimeZone(TimeZone.getDefault());
        locationHistoryRepository.save(new LocationHistory(wardrobe, clothes, clothes.getLocation(), df.format(System.currentTimeMillis())));
    }

    @Transactional
    public void clearClothes() {
        List<WardrobeClothes> records = wardrobeClothesRepository.findAllByWardrobe(this.wardrobe);

        for(WardrobeClothes record:records){

            Clothes clothes = record.getClothes();

            wardrobeClothesRepository.deleteByWardrobeAndClothes(wardrobe, clothes);
            locationHistoryRepository.deleteAllByWardrobeAndClothes(wardrobe, clothes);

            List<Outfit> outfits = outfitRepository.findAllByWardrobeAndClothes(wardrobe, clothes);

            if (outfits.size()==0){
                clothesRepository.deleteById(clothes.getId());
            }
        }
    }

    public int countClothes() {
        List<WardrobeClothes> records = wardrobeClothesRepository.findAllByWardrobe(this.wardrobe);
        return records.size();
    }

    public Clothes getClothes(int index){
        List<WardrobeClothes> records = wardrobeClothesRepository.findAllByWardrobe(this.wardrobe);
        for(int i=0;i<records.size();i++){
            if(i == index)
                return records.get(i).getClothes();
        }
        return null;
    }

    public List<Clothes> getAllClothes(){
        List<WardrobeClothes> wardrobeClothes = wardrobeClothesRepository.findAllByWardrobe(this.wardrobe);
        List<Clothes> my_clothes = new ArrayList<>();
        for (WardrobeClothes c: wardrobeClothes){
            my_clothes.add(c.getClothes());
        }
        return my_clothes;
    }

    public List<Clothes> getClothesByCategory(int categoryID) {
        Category category = categoryRepository.findById(categoryID);

        if (category == null){
            throw new BaseException("categoryID 不存在");
        }

        List<Clothes> clothes = new ArrayList<>();

        List<WardrobeClothes> records = wardrobeClothesRepository.findAllByWardrobe(this.wardrobe);
        for (WardrobeClothes record : records) {
            if (Objects.equals(record.getClothes().getCategoryName(), category.getName())) {
                clothes.add(record.getClothes());
            }
        }

        return clothes;
    }

    public List<Clothes> getAllByLocation(User user,String location){
        init(user);
        List<Clothes> clothes = new ArrayList<>();

        List<WardrobeClothes> records = wardrobeClothesRepository.findAllByWardrobe(this.wardrobe);
        for (WardrobeClothes record : records) {
            if (record.getClothes().getLocation().equals(location)) {
                clothes.add(record.getClothes());
            }
        }
        return clothes;
    }

    public List<Clothes> getAllBySeason(User user,String season){
        init(user);
        List<Clothes> clothes = new ArrayList<>();

        List<WardrobeClothes> records = wardrobeClothesRepository.findAllByWardrobe(this.wardrobe);
        for (WardrobeClothes record : records) {
            if (record.getClothes().getSeason().equals(season)) {
                clothes.add(record.getClothes());
            }
        }
        return clothes;
    }

    public Clothes getClothesByID(int clothesID){
        return clothesRepository.findById(clothesID);
    }

    @Transactional
    public void deleteClothes(int clothesID) {
        Clothes clothes = clothesRepository.findById(clothesID);

        if (clothes == null){
            throw new BaseException("clothesID 不存在");
        }

        wardrobeClothesRepository.deleteByWardrobeAndClothes(this.wardrobe, clothes);

        List<Outfit> outfits = outfitRepository.findAllByWardrobeAndClothes(wardrobe, clothes);

        if (outfits.size()==0){
            locationHistoryRepository.deleteAllByWardrobeAndClothes(this.wardrobe, clothes);
            clothesRepository.deleteById(clothesID);
        }
    }

    public void modifyClothes(int clothesID, String categoryName, String color, String season, double price, String location, String note) {
        Clothes clothes = clothesRepository.findById(clothesID);

        if (clothes == null){
            throw new BaseException("clothesID 不存在");
        }

        if (categoryRepository.findByWardrobeAndName(wardrobe, categoryName) == null){
            categoryRepository.save(new Category(wardrobe, categoryName));
        }

        if (locationRepository.findByWardrobeAndName(wardrobe, location) == null){
            locationRepository.save(new Location(wardrobe, location));
        }

        int flag = 0;

        clothes.setCategoryName(categoryName);
        clothes.setColor(color);
        clothes.setSeason(season);
        clothes.setPrice(price);
        if (!Objects.equals(clothes.getLocation(), location)){
            clothes.setLocation(location);
            flag = 1;
        }
        clothes.setNote(note);

        clothesRepository.save(clothes);

        if (flag == 1){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            df.setTimeZone(TimeZone.getDefault());
            locationHistoryRepository.save(new LocationHistory(wardrobe, clothes, location, df.format(System.currentTimeMillis())));
        }
    }

    /**
     * category相关
     */

    @Transactional
    public void clearCategory(){
        List<Category> records = categoryRepository.findAllByWardrobe(this.wardrobe);

        for(Category record:records){
            if (!Objects.equals(record.getName(), "未分类")){
                categoryRepository.delete(record);
            }
        }

        List<WardrobeClothes> wardrobeClothes = wardrobeClothesRepository.findAllByWardrobe(wardrobe);
        for (WardrobeClothes wardrobeCloth: wardrobeClothes){
            if (!Objects.equals(wardrobeCloth.getClothes().getCategoryName(), "未分类")){
                wardrobeCloth.getClothes().setCategoryName("未分类");
                clothesRepository.save(wardrobeCloth.getClothes());
            }
        }
    }

    public List<Category> getCategories(){
        return categoryRepository.findAllByWardrobe(wardrobe);
    }

    public void addCategory(String categoryName){
        if (categoryRepository.findByWardrobeAndName(wardrobe, categoryName) != null){
            throw new BaseException("category "+categoryName+" has existed");
        }

        categoryRepository.save(new Category(wardrobe, categoryName));
    }


    @Transactional
    public void editCategory(String categoryName,String newName){
        if (categoryRepository.findByWardrobeAndName(wardrobe, categoryName) == null){
            throw new BaseException("category "+categoryName+" not exists");
        }

        if(newName == null || newName.equals("")){
            throw new BaseException("newName is empty");
        }

        Category category = categoryRepository.findByWardrobeAndName(wardrobe, categoryName);
        category.setName(newName);
        categoryRepository.save(category);

        //将原来分类下的衣服转移到新分类下
        List<Clothes> clothes = getAllClothes();
        for (Clothes c:clothes){
            if (Objects.equals(c.getCategoryName(), categoryName)){
                c.setCategoryName(newName);
            }
        }
    }

    @Transactional
    public void deleteCategory(String categoryName){
        if (categoryRepository.findByWardrobeAndName(wardrobe, categoryName) == null){
            throw new BaseException("category "+categoryName+" not exists");
        }

        Category category = categoryRepository.findByWardrobeAndName(wardrobe, categoryName);
        categoryRepository.delete(category);

        //将原来分类下的衣服转移到未分类下
        List<Clothes> clothes = getAllClothes();
        for (Clothes c:clothes){
            if (Objects.equals(c.getCategoryName(), categoryName)){
                c.setCategoryName("未分类");
                clothesRepository.save(c);
            }
        }
    }

    public int countCategory(){
        List<Category> list = categoryRepository.findAllByWardrobe(wardrobe);
        return list.size();
    }

    public Category getCategory(int index){
        List<Category> list = categoryRepository.findAllByWardrobe(wardrobe);
        return list.get(index);
    }

    /**
     * 搜索功能
     */

    public List<Clothes> searchByAttribute(String categoryName, String season, String color, String priceRange, String location) {
        List<Clothes> clothes = getAllClothes();

        //对于每个属性找符合条件的衣物
        List<Clothes> clothes_category = new ArrayList<>();
        if (categoryName == null || categoryName.equals("")){
            clothes_category.addAll(clothes);
        }else {
            for (Clothes c: clothes){
                if (Objects.equals(c.getCategoryName(), categoryName)){
                    clothes_category.add(c);
                }
            }
        }

        List<Clothes> clothes_season = new ArrayList<>();
        if (season == null || season.equals("")){
            clothes_season.addAll(clothes);
        }else {
            for (Clothes c: clothes){
                if (Objects.equals(c.getSeason(), season)){
                    clothes_season.add(c);
                }
            }
        }

        List<Clothes> clothes_color = new ArrayList<>();
        if (color == null || color.equals("")){
            clothes_color.addAll(clothes);
        }else {
            for (Clothes c: clothes){
                if (Objects.equals(c.getColor(), color)){
                    clothes_color.add(c);
                }
            }
        }

        List<Clothes> clothes_price = new ArrayList<>();
        if (priceRange == null || priceRange.equals("")){
            clothes_price.addAll(clothes);
        }else {
            String[] prices = priceRange.split(",");
            double price1 = Double.parseDouble(prices[0]);
            double price2 = Double.parseDouble(prices[1]);

            for (Clothes c: clothes){
                if (c.getPrice() >= price1 && c.getPrice() <= price2){
                    clothes_price.add(c);
                }
            }
        }

        List<Clothes> clothes_location = new ArrayList<>();
        if (location == null || location.equals("")){
            clothes_location.addAll(clothes);
        }else {
            for (Clothes c: clothes){
                if (Objects.equals(c.getLocation(), location)){
                    clothes_location.add(c);
                }
            }
        }

        //找交集
        List<Clothes> result = new ArrayList<>();
        for (Clothes c: clothes_category){
            if (clothes_color.contains(c) && clothes_season.contains(c) && clothes_price.contains(c) && clothes_location.contains(c)){
                result.add(c);
            }
        }

        return result;
    }

    public List<Clothes> searchByKeyword(String keyword) {
        if (Objects.equals(keyword, "")){
            throw new BaseException("关键词不能为空");
        }

        List<Clothes> clothes = new ArrayList<>();

        //获取衣橱内所有衣服
        List<Clothes> my_clothes = getAllClothes();

        //模糊搜索
        for (Clothes c: my_clothes){
            if (c.getCategoryName().contains(keyword) ||
            c.getSeason().contains(keyword) ||
            c.getColor().contains(keyword) ||
            c.getLocation().contains(keyword) ||
            c.getNote().contains(keyword)){
                clothes.add(c);
            }
        }

        return clothes;
    }


    /**
     * 位置相关
     */
    public List<Location> getLocations(){
        return locationRepository.findAllByWardrobe(wardrobe);
    }

    public List<LocationHistory> getLocationHistory(int clothesID) {
        Clothes clothes = clothesRepository.findById(clothesID);
        if (clothes == null){
            throw new BaseException("clothesID 不存在");
        }

        return locationHistoryRepository.findAllByWardrobeAndClothes(wardrobe, clothes);
    }

    @Transactional
    public void clearLocation(){
        List<Location> locations = locationRepository.findAllByWardrobe(wardrobe);
        for(Location location: locations){
            if (!Objects.equals(location.getName(), "默认") && !Objects.equals(location.getName(), "卧室衣柜") && !Objects.equals(location.getName(), "衣帽间") && !Objects.equals(location.getName(), "鞋柜")){
                locationRepository.delete(location);

                //将原来位置下的衣服转移到默认下
                List<Clothes> clothes = getAllClothes();
                for (Clothes c:clothes){
                    c.setLocation("默认");
                    clothesRepository.save(c);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
                    df.setTimeZone(TimeZone.getDefault());
                    locationHistoryRepository.save(new LocationHistory(wardrobe, c, c.getLocation(), df.format(System.currentTimeMillis())));
                }
            }
        }
    }

    @Transactional
    public void deleteLocation(String locationName){
        if (Objects.equals(locationName, "默认")){
            throw new BaseException("默认位置无法删除");
        }

        Location location = locationRepository.findByWardrobeAndName(wardrobe, locationName);
        if(location == null){
            throw new BaseException("location not exit");
        }

        locationRepository.delete(location);

        //将原来位置下的衣服转移到默认下
        List<Clothes> clothes = getAllClothes();
        for (Clothes c:clothes){
            if (Objects.equals(c.getLocation(), locationName)){
                c.setLocation("默认");
                clothesRepository.save(c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
                df.setTimeZone(TimeZone.getDefault());
                locationHistoryRepository.save(new LocationHistory(wardrobe, c, c.getLocation(), df.format(System.currentTimeMillis())));
            }
        }
    }

    /**
     * 穿搭相关
     */
    @Transactional
    public void clearOutfit(){
        List<Outfit> outfits = outfitRepository.findAllByWardrobe(wardrobe);
        outfitRepository.deleteAll(outfits);
    }

    public void addOutfit(String outfitName, List<Integer> clothes_IDs) {
        if (outfitName == null || outfitName.equals("")){
            throw new BaseException("the outfitName is empty");
        }

        List<Clothes> clothes = new ArrayList<>();
        for (Integer id: clothes_IDs){
            Clothes c = clothesRepository.findById(id).orElse(null);
            if (c == null){
                throw new BaseException("clothesID 不存在");
            }
            clothes.add(c);
        }

        outfitRepository.save(new Outfit(wardrobe, outfitName, null));
        for (Clothes c: clothes){
            outfitRepository.save(new Outfit(wardrobe, outfitName, c));
        }
    }

    public List<Outfit> getOutfits() {
        return outfitRepository.findAllByWardrobe(wardrobe);
    }

    @Transactional
    public void deleteOutfit(String outfitName) {
        if (outfitName == null || outfitName.equals("")){
            throw new BaseException("the outfitName is empty");
        }

        List<Outfit> outfits = outfitRepository.findAllByWardrobeAndOutfitName(wardrobe, outfitName);

        if (outfits.size() == 0){
            throw new BaseException("the outfit not exists");
        }

        outfitRepository.deleteAll(outfits);
    }

    public void addClothesIntoOutfit(String outfitName, List<Integer> clothes_IDs) {
        if (outfitName == null || outfitName.equals("")){
            throw new BaseException("the outfitName is empty");
        }

        List<Clothes> clothes = new ArrayList<>();
        for (Integer id: clothes_IDs){
            Clothes c = clothesRepository.findById(id).orElse(null);
            if (c == null){
                throw new BaseException("clothesID 不存在");
            }
            clothes.add(c);
        }

        for (Clothes c: clothes){
            Outfit outfit = outfitRepository.findByWardrobeAndOutfitNameAndClothes(wardrobe, outfitName, c);
            if (outfit == null){
                outfitRepository.save(new Outfit(wardrobe, outfitName, c));
            }
        }
    }

    @Transactional
    public void deleteClothesFromOutfit(String outfitName, List<Integer> clothes_IDs) {
        if (outfitName == null || outfitName.equals("")){
            throw new BaseException("the outfitName is empty");
        }

        List<Clothes> clothes = new ArrayList<>();
        for (Integer id: clothes_IDs){
            Clothes c = clothesRepository.findById(id).orElse(null);
            if (c == null){
                throw new BaseException("clothesID 不存在");
            }
            clothes.add(c);
        }

        for (Clothes c: clothes){
            Outfit outfit = outfitRepository.findByWardrobeAndOutfitNameAndClothes(wardrobe, outfitName, c);
            if (outfit != null){
                outfitRepository.delete(outfit);
            }
        }
    }
}
