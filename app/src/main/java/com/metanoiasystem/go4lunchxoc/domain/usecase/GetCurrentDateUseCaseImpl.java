package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.GetCurrentDateUseCase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetCurrentDateUseCaseImpl implements GetCurrentDateUseCase {
    @Override
    public String execute() {
        return new SimpleDateFormat("dd/MM/yy").format(new Date());
    }
}

