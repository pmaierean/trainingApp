/*
 * =========================================================================================
 * Copyright (c) 2024 - 2025 to Maiereni Software and Consulting Inc
 * =========================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.maiereni.webapp.sample.controller.rest;

import com.maiereni.webapp.sample.bo.BaseData;
import com.maiereni.webapp.sample.bo.BaseDatas;
import com.maiereni.webapp.sample.bo.request.DataRequest;
import com.maiereni.webapp.sample.bo.response.BaseResponse;
import com.maiereni.webapp.sample.bo.response.DataResponse;
import com.maiereni.webapp.sample.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller that responds to
 * @author Petre Maierean
 * @date 1/7/2025 7:27 AM
 **/
@RestController
@RequestMapping("/v1/api")
@CrossOrigin(origins = "*")
@Slf4j
public class IndexController {
    private final DataService dataService;
    public IndexController(final DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(path="/index")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BaseResponse> index() {
        return ResponseEntity.ok(new BaseResponse());
    }

    @PostMapping(path="/data")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BaseResponse> setData(@RequestBody DataRequest dataRequest) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            dataService.saveData(dataRequest);
        }
        catch (Exception e) {
            log.error("Failed to persist data", e);
            baseResponse.setMessage("Failed to persist data");
            baseResponse.setCode(-1);
        }
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path="/data")
    @PreAuthorize("permitAll()")
    public ResponseEntity<DataResponse<BaseDatas>> getData(@RequestParam(name="selector") Optional<String> selector) {
        DataResponse<BaseDatas> response = new DataResponse<>();
        try {
            String filter = ".*";
            if (selector.isPresent()) {
                filter = selector.get();
            }
            BaseDatas data = new BaseDatas();
            List<BaseData> bd = dataService.findData(filter);
            data.setData(bd);
            response.setData(data);
        }
        catch (Exception e) {
            log.error("Failed to retrieve data", e.getMessage());
            response.setMessage("Failed to retrieve data");
            response.setCode(-1);
        }

        return ResponseEntity.ok(response);
    }
}
