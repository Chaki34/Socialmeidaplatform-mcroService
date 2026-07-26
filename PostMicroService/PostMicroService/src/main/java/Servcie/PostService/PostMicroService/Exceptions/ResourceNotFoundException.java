package Servcie.PostService.PostMicroService.Exceptions;



public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}