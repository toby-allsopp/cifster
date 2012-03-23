RES_DIR = ../res
DRAWABLE_BASE = $(RES_DIR)/drawable
ART_DIR = .

.PHONY: all

DPIS = ldpi mdpi hdpi xhdpi
dp_to_px_xhdpi = $(shell expr $(1) \* 2)
dp_to_px_hdpi = $(shell expr \( $(1) \* 3 \) / 2)
dp_to_px_mdpi = $(1)
dp_to_px_ldpi = $(shell expr \( $(1) \* 3 \) / 4)

define deficon_dpi
$(DRAWABLE_BASE)-$(4)/$(1).png: WIDTH = $$(call dp_to_px_$(4),$(2))
$(DRAWABLE_BASE)-$(4)/$(1).png: HEIGHT = $$(call dp_to_px_$(4),$(3))
all: $(DRAWABLE_BASE)-$(4)/$(1).png
endef

deficon = $(foreach dpi,$(DPIS),$(eval $(call deficon_dpi,$(1),$(2),$(3),$(dpi))))

$(call deficon,ic_launcher,48,48)

DPI_RULE = $(RES_DIR)/drawable-$(dpi)/%.png: $(ART_DIR)/%.svg; inkscape -e $$@ -w $$(WIDTH) -h $$(HEIGHT) $$<

$(foreach dpi,$(DPIS),$(eval $(DPI_RULE)))

