#!/bin/bash

# Define the string to search for
SEARCH_STRING="3005"

# Get the list of all branches (both local and remote)
branches=$(git branch -r | grep -v '\->' | sed 's/origin\///')

# Create a temporary file to store the results
temp_file=$(mktemp)

# Iterate over each branch
for branch in $branches; do
    echo "Checking branch: $branch"
    
    # Check out the branch
    git checkout "$branch" > /dev/null 2>&1
    
    # Search for the string in the files of the current branch
    grep -rnl . -e "$SEARCH_STRING" > "$temp_file"
    
    # Check if the search result is not empty
    if [[ -s "$temp_file" ]]; then
        echo "Found in branch $branch:"
        cat "$temp_file"
    else
        echo "Not found in branch $branch"
    fi
    
    echo "--------------------------"
done

# Clean up
rm "$temp_file"
# Return to the original branch
git checkout @{-1} > /dev/null 2>&1

echo "Finished checking all branches."

