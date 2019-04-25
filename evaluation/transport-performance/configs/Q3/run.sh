find . -type f -name '*.morph.properties' -exec sed -i '' s/\\/mappings\\//''/ {} +
find . -type f -name '*.morph.properties' -exec sed -i '' s/\\/queries\\//''/ {} +
find . -type f -name '*.morph.properties' -exec sed -i '' s/\\/results\\//''/ {} +
