# peresed

Based on https://github.com/avast/pe_tools/blob/master/pe_tools/peresed.py but some imports are modified to allow packaging into an executable file.

## Building
    # For Linux/macOS
    pip install pyinstaller
    pip install pe_tools
    pyinstaller --onefile peresed.py

    # For Windows
    # Windows Defender believes that the executable file created with PyInstaller/py2exe contains a virus
    # Executable file created with Nuitka works fine on Windows 10 but dont work on Windows 7
    # Therefore, it is impossible to use peresed on Windows and you will have to use ResourceHacker