package com.financely.tracker.dto;

public class ApiResponse<T>{
    private boolean success;
    private T data;
    private String errorMessage;

    public ApiResponse(T data){
        this.success = true;
        this.data = data;
        this.errorMessage = null;
    }

    public ApiResponse(String errorMessage){
        this.success = false;
        this.data = null;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
