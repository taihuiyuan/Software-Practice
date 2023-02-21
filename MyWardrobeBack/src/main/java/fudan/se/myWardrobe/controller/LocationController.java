package fudan.se.myWardrobe.controller;

import fudan.se.myWardrobe.controller.dto.LocationDTO;
import fudan.se.myWardrobe.controller.request.EditLocationRequest;
import fudan.se.myWardrobe.controller.request.LocationRequest;
import fudan.se.myWardrobe.controller.response.ErrorResponse;
import fudan.se.myWardrobe.controller.response.SuccessResponse;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class LocationController {

    @Resource
    private LocationService locationService;

    @GetMapping("/location/get")
    public LocationDTO getLocation(@RequestParam("username") String username){
        return locationService.getLocation(username);
    }

    @PostMapping("/location/add")
    public ResponseEntity<?> addLocation(@RequestBody LocationRequest request){
        try{
            locationService.addLocation(request.getUsername(), request.getLocationName());
            SuccessResponse successResponse = new SuccessResponse("add successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/location/modify")
    public ResponseEntity<?> modifyLocation(@RequestBody EditLocationRequest request){
        try{
            locationService.modifyLocation(request.getUsername(), request.getLocationName(), request.getNewLocationName());
            SuccessResponse successResponse = new SuccessResponse("modify successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/location/delete")
    public ResponseEntity<?> deleteLocation(@RequestBody LocationRequest request){
        try{
            locationService.deleteLocation(request.getUsername(), request.getLocationName());
            SuccessResponse successResponse = new SuccessResponse("delete successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


}
