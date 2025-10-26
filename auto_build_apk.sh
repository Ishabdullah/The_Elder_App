#!/bin/bash

# === CONFIG ===
REPO="Ishabdullah/The_Elder_App"
WORKFLOW_FILE="build_and_release.yml"  # Make sure this matches exactly your workflow file
BRANCH="main"
TOKEN="$GITHUB_TOKEN"  # Make sure your GitHub token is set in Termux environment

# === CHECK TOKEN ===
if [ -z "$TOKEN" ]; then
    echo "[!] GitHub token not set. Run: export GITHUB_TOKEN=your_token_here"
    exit 1
fi

# === Pull latest changes ===
echo "[*] Repo exists, pulling latest changes..."
cd ~/The_Elder_App || exit
git fetch origin
git reset --hard origin/$BRANCH

# === Trigger GitHub Actions workflow ===
echo "[*] Triggering workflow..."
RESPONSE=$(curl -s -X POST \
-H "Accept: application/vnd.github+json" \
-H "Authorization: Bearer $TOKEN" \
https://api.github.com/repos/$REPO/actions/workflows/$WORKFLOW_FILE/dispatches \
-d "{\"ref\":\"$BRANCH\"}")

if echo "$RESPONSE" | grep -q "Not Found"; then
    echo "[!] Workflow file not found or repo/branch wrong!"
    exit 1
fi

echo "[*] Workflow triggered successfully."

# === Polling workflow status ===
echo "[*] Waiting for workflow to finish..."
while true; do
    STATUS=$(curl -s -H "Authorization: Bearer $TOKEN" \
    https://api.github.com/repos/$REPO/actions/runs?branch=$BRANCH | jq -r '.workflow_runs[0].conclusion')
    
    if [ "$STATUS" != "null" ]; then
        echo "[*] Workflow finished with status: $STATUS"
        break
    fi
    echo "[*] Still waiting..."
    sleep 10
done

# === Optional: Download APK if released ===
# Replace 'app-release.apk' path if different in your workflow
APK_URL=$(curl -s -H "Authorization: Bearer $TOKEN" \
https://api.github.com/repos/$REPO/actions/artifacts | jq -r '.artifacts[0].archive_download_url')

if [ "$APK_URL" != "null" ]; then
    echo "[*] Downloading APK..."
    curl -L -H "Authorization: Bearer $TOKEN" -o ~/Downloads/app-release.zip "$APK_URL"
    echo "[*] APK downloaded as ~/Downloads/app-release.zip"
else
    echo "[!] No APK artifact found."
fi
