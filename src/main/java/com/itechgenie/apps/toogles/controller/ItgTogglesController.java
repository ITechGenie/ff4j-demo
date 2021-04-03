package com.itechgenie.apps.toogles.controller;

import static org.ff4j.web.FF4jWebConstants.ROLE_READ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.ff4j.FF4j;
import org.ff4j.core.FlippingExecutionContext;
import org.ff4j.exception.FeatureNotFoundException;
import org.ff4j.services.domain.FeatureApiBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/toggles")
public class ItgTogglesController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItgTogglesController.class);

	@Autowired
	private FF4j ff4j;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ ROLE_READ })
	@ApiOperation(value = "Read information about a feature with custom context data", response = FeatureApiBean.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Information about features"),
			@ApiResponse(code = 404, message = "Feature not found") })
	@RequestMapping(method = RequestMethod.GET, value = "feature")
	public Object getFeature(@RequestHeader("uid") String id, @RequestHeader("AM_USERID") String amUserId,
			@RequestHeader("AM_ECPDID") String amEcpdId) {
		if (!ff4j.getFeatureStore().exist(id)) {
			String errMsg = new FeatureNotFoundException(id).getMessage();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
		}

		FlippingExecutionContext fex = new FlippingExecutionContext();
		fex.addValue("AM_USERID", amUserId);
		fex.addValue("AM_ECPDID", amEcpdId);
		fex.addValue("clientHostName", amEcpdId);
		

		Boolean featureEnabled = ff4j.check(id, fex);

		LOGGER.debug("Feature for user: " + amUserId + " and for ECPD: " + amEcpdId + " -- Value: " + featureEnabled);

		if (!featureEnabled) {
			String errMsg = new FeatureNotFoundException(id).getMessage();
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, errMsg);
		}

		return new FeatureApiBean(ff4j.getFeatureStore().read(id));
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	// @RolesAllowed({ ROLE_READ })
	@ApiOperation(value = "Read information about a feature with custom context data")
	@ApiResponses({ @ApiResponse(code = 200, message = "Information about features"),
			@ApiResponse(code = 404, message = "Feature not found") })
	@RequestMapping(method = RequestMethod.GET, value = "dummy")
	public Object read(@PathVariable("uid") String id, @RequestHeader("AM_USERID") String amUserId,
			@RequestHeader("AM_ECPDID") String amEcpdId) {
		LOGGER.info("Inside ItgTogglesController.read: " + id + " - " + " am_userid: " + amUserId + "AM_ECPID: "
				+ amEcpdId);
		Map<String, Object> app = new HashMap<String, Object>();

		List<String> apps = new ArrayList<String>();
		apps.add("amUserId " + amUserId);
		apps.add("amEcpdId " + amEcpdId);

		app.put("data", apps);
		app.put("success", true);

		LOGGER.info("Returning: " + app);
		return app;
	}

}
