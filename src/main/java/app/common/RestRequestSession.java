package app.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.Builder;
import lombok.Getter;

@Getter
@RequestScope
@Builder(setterPrefix="set")
@Component
public class RestRequestSession {

	private long deviceID;
	private long userID;
	private String uniqueID;

}
