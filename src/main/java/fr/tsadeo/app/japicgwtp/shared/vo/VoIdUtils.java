package fr.tsadeo.app.japicgwtp.shared.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VoIdUtils {

    /*
	 * Transforms a list of IVoId in a map[id, item]
     */
    public static <T extends IVoId> Map<Long, T> getMapId2Item(List<T> listItems) {
        
        
        if (listItems != null) {
            
            Map<Long, T> map = new HashMap<>(listItems.size());
            for (T item : listItems) {
                map.put(item.getId(), item);
            }
            return map;
            
//          return  listItems.stream()
//                    .collect(Collectors.toMap(item -> item.getId(), item -> item));
        }
        else {
            return new HashMap<>(0);
        }

    }
    
    public static boolean isIdUndefined(Long id) {
        return Objects.isNull(id) || id == IVoId.ID_UNDEFINED;
                
    }
    

}
