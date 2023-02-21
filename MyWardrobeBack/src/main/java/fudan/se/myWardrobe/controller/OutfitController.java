package fudan.se.myWardrobe.controller;

import fudan.se.myWardrobe.controller.dto.OutfitDTO;
import fudan.se.myWardrobe.controller.request.DeleteOutfitRequest;
import fudan.se.myWardrobe.controller.request.OutfitRequest;
import fudan.se.myWardrobe.controller.response.ErrorResponse;
import fudan.se.myWardrobe.controller.response.SuccessResponse;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.service.OutfitServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class OutfitController {
    @Resource
    private OutfitServiceImpl outfitService;

    @PostMapping("/outfit/add")
    public ResponseEntity<?> addOutfit(@RequestBody OutfitRequest request){
        try{
            outfitService.addOutfit(request.getUsername(), request.getOutfitName(), request.getClothes());
            SuccessResponse successResponse = new SuccessResponse("add successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/outfit/get")
    public OutfitDTO getOutfits(@RequestParam("username") String username){
        return outfitService.getOutfits(username);
    }

    @PostMapping("/outfit/delete")
    public ResponseEntity<?> deleteOutfit(@RequestBody DeleteOutfitRequest request){
        try{
            outfitService.deleteOutfit(request.getUsername(), request.getOutfitName());
            SuccessResponse successResponse = new SuccessResponse("delete successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/outfit/addClothes")
    public ResponseEntity<?> addClothesIntoOutfit(@RequestBody OutfitRequest request){
        try{
            outfitService.addClothesIntoOutfit(request.getUsername(), request.getOutfitName(), request.getClothes());
            SuccessResponse successResponse = new SuccessResponse("add clothes successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/outfit/deleteClothes")
    public ResponseEntity<?> deleteClothesFromOutfit(@RequestBody OutfitRequest request){
        try{
            outfitService.deleteClothesFromOutfit(request.getUsername(), request.getOutfitName(), request.getClothes());
            SuccessResponse successResponse = new SuccessResponse("delete clothes successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
