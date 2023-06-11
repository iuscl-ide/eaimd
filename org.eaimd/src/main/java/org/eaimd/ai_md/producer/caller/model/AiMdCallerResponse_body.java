/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.producer.caller.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AiMdCallerResponse_body {

	String id;
	
	String object;
	
	long created;
	
	String model;
	
	AiMdCallerResponse_usage usage;
	
	List<AiMdCallerResponse_choice> choices = new ArrayList<>();
}
