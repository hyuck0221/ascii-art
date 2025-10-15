const uploadArea = document.getElementById('uploadArea');
const fileInput = document.getElementById('fileInput');
const canvas = document.getElementById('canvas');
const status = document.getElementById('status');
const renderMode = document.getElementById('renderMode');
const artType = document.getElementById('artType');
const threadType = document.getElementById('threadType');
const reverse = document.getElementById('reverse');
const fontSizeInput = document.getElementById('fontSize');
const fontSizeValue = document.getElementById('fontSizeValue');

let currentEventSource = null;
let currentId = null;
let linesArray = [];

fontSizeInput.addEventListener('input', (e) => {
    const size = e.target.value / 10;
    fontSizeValue.textContent = size.toFixed(1) + 'px';
    canvas.style.fontSize = size + 'px';
});

uploadArea.addEventListener('click', () => fileInput.click());

uploadArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    uploadArea.classList.add('dragover');
});

uploadArea.addEventListener('dragleave', () => {
    uploadArea.classList.remove('dragover');
});

uploadArea.addEventListener('drop', (e) => {
    e.preventDefault();
    uploadArea.classList.remove('dragover');
    const files = e.dataTransfer.files;
    if (files.length > 0) {
        handleFile(files[0]);
    }
});

fileInput.addEventListener('change', (e) => {
    if (e.target.files.length > 0) {
        handleFile(e.target.files[0]);
    }
});

async function handleFile(file) {
    if (!file.type.startsWith('image/')) {
        showError('Please select an image file');
        return;
    }

    const maxSizeInMB = 30;
    const maxSizeInBytes = maxSizeInMB * 1024 * 1024;

    if (file.size > maxSizeInBytes) {
        showError(`File size exceeds ${maxSizeInMB}MB limit`);
        return;
    }

    canvas.innerHTML = '';

    const resizedFile = await resizeImageIfNeeded(file);

    if (renderMode.value === 'realtime') {
        await handleRealtimeRender(resizedFile);
    } else {
        await handleBatchRender(resizedFile);
    }
}

async function resizeImageIfNeeded(file) {
    return new Promise((resolve) => {
        const img = new Image();
        const reader = new FileReader();

        reader.onload = (e) => {
            img.onload = () => {
                const maxWidth = 1920;
                const maxHeight = 1080;

                if (img.width <= maxWidth && img.height <= maxHeight) {
                    resolve(file);
                    return;
                }

                const canvas = document.createElement('canvas');
                const ctx = canvas.getContext('2d');

                const widthRatio = maxWidth / img.width;
                const heightRatio = maxHeight / img.height;
                const ratio = Math.min(widthRatio, heightRatio);

                canvas.width = img.width * ratio;
                canvas.height = img.height * ratio;

                ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

                canvas.toBlob((blob) => {
                    const resizedFile = new File([blob], file.name, {
                        type: file.type,
                        lastModified: Date.now()
                    });
                    resolve(resizedFile);
                }, file.type, 0.9);
            };

            img.src = e.target.result;
        };

        reader.readAsDataURL(file);
    });
}

async function handleRealtimeRender(file) {
    if (currentEventSource) {
        currentEventSource.close();
    }

    linesArray = [];
    showLoading();
    status.textContent = 'Connecting to SSE...';

    currentEventSource = new EventSource('/ascii-art/generate/async/connect');

    currentEventSource.addEventListener('ID', async (e) => {
        const data = JSON.parse(e.data);
        currentId = data.payload;
        canvas.textContent = '';
        status.textContent = 'Connected. Uploading image...';
        await uploadImage(file, currentId);
    });

    currentEventSource.addEventListener('PRINT_LINE', (e) => {
        const data = JSON.parse(e.data);
        renderLine(data.payload);
        status.textContent = 'Rendering...';
    });

    currentEventSource.addEventListener('error', () => {
        currentEventSource.close();
        status.textContent = 'Complete';
    });
}

async function uploadImage(file, id) {
    const formData = new FormData();
    formData.append('image', file);

    const params = new URLSearchParams({
        id: id,
        artType: artType.value,
        reverse: reverse.checked,
        threadType: threadType.value
    });

    try {
        await fetch(`/ascii-art/generate/async/upload?${params}`, {
            method: 'POST',
            body: formData
        });
    } catch (error) {
        showError('Upload failed: ' + error.message);
    }
}

async function handleBatchRender(file) {
    showLoading();
    status.textContent = 'Processing...';

    const formData = new FormData();
    formData.append('image', file);

    const params = new URLSearchParams({
        artType: artType.value,
        reverse: reverse.checked,
        threadType: threadType.value
    });

    try {
        const response = await fetch(`/ascii-art/generate?${params}`, {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error('Generation failed');
        }

        const data = await response.json();
        canvas.innerHTML = '';
        data.forEach(line => renderLine(line));
        status.textContent = 'Complete';
    } catch (error) {
        showError('Generation failed: ' + error.message);
    }
}

function renderLine(lineData) {
    const { y, chars } = lineData;

    while (linesArray.length <= y) {
        linesArray.push('');
    }

    linesArray[y] = chars;
    canvas.textContent = linesArray.join('\n');
}

function showLoading() {
    canvas.innerHTML = `
        <div class="loading">
            <div class="spinner"></div>
            <div>Generating ASCII art...</div>
        </div>
    `;
}

function showError(message) {
    canvas.innerHTML = `<div class="error">${message}</div>`;
    status.textContent = 'Error';
}