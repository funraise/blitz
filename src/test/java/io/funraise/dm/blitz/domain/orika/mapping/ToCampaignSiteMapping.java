package io.funraise.dm.blitz.domain.orika.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.orika.source.FromCampaignPage;
import io.funraise.dm.blitz.domain.orika.source.FromCampaignSite;
import io.funraise.dm.blitz.domain.orika.target.ToCampaignSite;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@MappingTo(ToCampaignSite.class)
public class ToCampaignSiteMapping {

    @Mapping(originalClass = FromCampaignSite.class)
    public Map<String, Function<FromCampaignSite, ?>> getMapping() {
        Map<String, Function<FromCampaignSite, ?>> mapping = new HashMap<>();
        mapping.put("homepage", site -> {
            FromCampaignPage page = site.getHomepage();

            if (page != null) {
                return page.isHomepage();
            }
            else {
                return false;
            }
        });

        return mapping;
    }
}
