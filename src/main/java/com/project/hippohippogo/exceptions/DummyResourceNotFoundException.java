package com.project.hippohippogo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DummyResourceNotFoundException extends RuntimeException {
    private Long _id;

    public DummyResourceNotFoundException(Long _id) {
        super(String.format("Dummy item #%d does not exist", _id));
        this._id = _id;
    }

    public Long get_id() {
        return _id;
    }

}