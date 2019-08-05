package com.axway.apim.prometheus.apiregistry;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axway.apim.swagger.APIManagerAdapter;
import com.axway.apim.swagger.api.state.IAPI;

import io.prometheus.client.Gauge;

public class APIRegistry {
	static Logger LOG = LoggerFactory.getLogger(APIRegistry.class);

	private final Gauge apiRegistryInfo = Gauge.build()
	        .name("api_registry_info")
	        .labelNames("path", "name", "version")
	        .help("The detected API-Manager version")
	        .register();

	public APIRegistry() {
		super();
		try {
			APIManagerAdapter apimAdapter = APIManagerAdapter.getInstance();
			
			List<IAPI> apis = apimAdapter.getAllAPIs();
			
			apiRegistryInfo.labels("Unknown", "Unknown API", "N/A");
			for(IAPI api : apis) {
				apiRegistryInfo.labels(api.getPath(), api.getName(), getAPIVersion(api));
			}
		} catch (Exception e) {
			LOG.error("Unable to initialize API-Manager Regsitry information", e);
			throw new RuntimeException("Unable to initialize API-Manager Regsitry information", e);
		}
	}
	
	private String getAPIVersion(IAPI api) {
		return (api.getVersion()==null) ? "N/A" : api.getVersion();
	}
}
