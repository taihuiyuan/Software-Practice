package fudan.se.myWardrobe.controller;

import fudan.se.myWardrobe.controller.dto.ClothesDTO;
import fudan.se.myWardrobe.controller.dto.HistoryDTO;
import fudan.se.myWardrobe.controller.dto.ImageDTO;
import fudan.se.myWardrobe.controller.dto.SingleClothesDTO;
import fudan.se.myWardrobe.controller.request.ClothesRequest;
import fudan.se.myWardrobe.controller.request.EditClothesRequest;
import fudan.se.myWardrobe.controller.request.SelectRequest;
import fudan.se.myWardrobe.controller.response.ErrorResponse;
import fudan.se.myWardrobe.controller.response.SuccessResponse;
import fudan.se.myWardrobe.entity.Clothes;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.service.ClothesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class ClothesController {

    @Resource
    private ClothesService clothesService;

    @CrossOrigin
    @PostMapping(value="clothes/image")
    public ImageDTO addClothesImage(@RequestParam("file") MultipartFile file) {
        String path = "/mnt/img";
        //String path = "C:/Users/22435/Desktop/img";
        File filePath = new File(path);
        System.out.println("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdir();
        }

        //获取原始文件名称(包含格式)
        String originalFileName = file.getOriginalFilename();
        originalFileName=originalFileName.replaceAll(",|&|=", "");
        System.out.println("原始文件名称：" + originalFileName);
        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("文件类型：" + type);
        if(!type.equals("jpg") && !type.equals("jpeg") && !type.equals("png")){
            throw new BaseException("please upload image end with .jpg/.jpeg/.png");
        }
        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        //设置文件新名称: 当前时间+文件名称（不包含格式）
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date = sdf.format(d);
        //String fileName ="a"+date + name + "." + type;
        String fileName =date + "." + type;
        System.out.println("新文件名称：" + fileName);
        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);
        try {
            file.transferTo(targetFile);
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setImageUrl("http://129.211.165.110:8001/api/file/" + targetFile.getName());
            return imageDTO;
            //return "http://127.0.0.1:8080/api/file/" + targetFile.getName();
        } catch (Exception e) {
            throw new BaseException("图片上传失败");
        }
    }

    @PostMapping("/clothes/add")
    public ResponseEntity<?> addClothes(@RequestBody ClothesRequest clothesRequest){
        try {
            clothesService.addClothes(clothesRequest);
            SuccessResponse successResponse = new SuccessResponse("add successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/clothes/getByCategory")
    public ClothesDTO getClothesByCategory(@RequestParam("username") String username, @RequestParam("categoryID") int categoryID){
        return clothesService.getClothesByCategory(username, categoryID);
    }

    @GetMapping("/clothes/getAllClothes")
    public ClothesDTO getAllClothes(@RequestParam("username") String username){
        return clothesService.getAllClothes(username);
    }

    @GetMapping("/clothes/getClothesByID")
    public SingleClothesDTO getClothesByID(@RequestParam("username") String username, @RequestParam("clothesID") int clothesID){
        return clothesService.getClothesByID(username, clothesID);
    }

    @GetMapping("/clothes/delete")
    public ResponseEntity<?> deleteClothes(@RequestParam("username") String username, @RequestParam("clothesID") int clothesID){
        try {
            clothesService.deleteClothes(username, clothesID);
            SuccessResponse successResponse = new SuccessResponse("delete successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/clothes/modify")
    public ResponseEntity<?> modifyClothes(@RequestBody EditClothesRequest request){
        try {
            clothesService.modifyClothes(request);
            SuccessResponse successResponse = new SuccessResponse("modify successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/clothes/searchByKeyword")
    public ClothesDTO searchByKeyword(@RequestParam("username") String username, @RequestParam("keyword") String keyword){
        return clothesService.searchByKeyword(username, keyword);
    }

    @PostMapping("/clothes/searchByAttribute")
    public ClothesDTO searchByAttribute(@RequestBody SelectRequest request){
        return clothesService.searchByAttribute(request);
    }

    @GetMapping("/clothes/locationHistory")
    public HistoryDTO getLocationHistory(@RequestParam("username") String username, @RequestParam("clothesID") int clothesID){
        return clothesService.getLocationHistory(username, clothesID);
    }
}
