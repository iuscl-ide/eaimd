/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.repo.model;

import java.util.LinkedHashMap;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiMdRepoReferences {

	LinkedHashMap<String, AiMdRepoReferenceTextActions> aiMdReferences = new LinkedHashMap<>(); 
}
