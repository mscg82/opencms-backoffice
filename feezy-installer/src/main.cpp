#include <windows.h>
#include <objidl.h>
#include <shlobj.h>
#include <vector>
#include <sstream>
#include <string>
#include <CurlClient.h>
#include <WriteToFileCurlWriteHandler.h>
#include "WindowsConsoleWriterStatisticsHandler.h"

using namespace std;

void DisplayErrorBox(string functionName, DWORD error = 0) {
    // Retrieve the system error message for the last-error code

    LPVOID lpMsgBuf;
    if(error == 0)
    	error = GetLastError();

    FormatMessage(
        FORMAT_MESSAGE_ALLOCATE_BUFFER |
        FORMAT_MESSAGE_FROM_SYSTEM |
        FORMAT_MESSAGE_IGNORE_INSERTS,
        NULL,
        error,
        MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
        (LPTSTR) &lpMsgBuf,
        0, NULL );

    // Display the error message and clean up

    stringstream message;
    message << functionName << " ha generato l'errore " << error << ":\n" << string((char*)lpMsgBuf);
    MessageBox(NULL, message.str().c_str(), TEXT("Errore"), MB_OK | MB_ICONERROR);

    LocalFree(lpMsgBuf);
}

vector<string> splitString(char *source, const string &delimiter) {
	vector<string> tokens;

	char *token = strtok(source, delimiter.c_str());
	while(token != NULL) {
		tokens.push_back(token);
		token = strtok(NULL, ".");
	}

	return tokens;
}

vector<string> splitString(const string &source, const string &delimiter) {
	char *orig = new char[source.length() + 1];
	for(int i = 0, l = source.length(); i < l; i++)
		orig[i] = source[i];
	orig[source.length()] = '\0';

	delete []orig;
	vector<string> ret = splitString(orig, delimiter);
	return ret;
}

bool launchProcess(string commandline, bool waitForProcess = true) {
	STARTUPINFO si;
	PROCESS_INFORMATION pi;
	ZeroMemory(&si, sizeof(si));
	si.cb = sizeof(si);
	ZeroMemory(&pi, sizeof(pi));

	char *tmp = new char[commandline.size()];
	for(int i = 0, l = commandline.size(); i < l; i++)
		tmp[i] = commandline[i];
	BOOL executed = CreateProcess(NULL, tmp, NULL, NULL, FALSE, 0, NULL, NULL, &si, &pi);
	delete []tmp;

	if(!executed) {
		DisplayErrorBox("CreateProcess");
		return false;
	}

	if(waitForProcess) {
		// Wait for the process to exit
		WaitForSingleObject(pi.hProcess, INFINITE);
	}

	// Close process and thread handles.
	CloseHandle(pi.hProcess);
	CloseHandle(pi.hThread);

	return true;
}

bool downloadAndInstallSilverlight(bool installed, string tempPath, CurlClient &client) {
	string message;
	if(installed)
		message = "E' necessario aggiornare la versione di Silverlight installata  sul tuo sistema.";
	else
		message = "E' necessario installare Silverlight sul tuo sistema.";

	message += "\nVerra' ora avviato il download del programma di installazione.\nTi preghiamo di chiudere ";
	message += "tutte le finestre dei browser (Internet Explorer, Firefox, Chrome, Opera, Safari, ecc.) ";
	message += "prima di procedere con l'operazione.\n\n";
	message += "Clicca su OK per continuare con il download e l'installazione oppure su Annulla per ";
	message += "interrompere il processo.";

	int ret = MessageBox(NULL, TEXT(message.c_str()), TEXT("Aggiornamento necessario"), MB_OKCANCEL | MB_ICONINFORMATION);
	if(ret != IDOK)
		return false;

	string installerFile(tempPath + "\\Silverlight.exe");


	client.setStatisticsHandler(new WindowsConsoleWriterStatisticsHandler("Percentuale di download dell'installer di Silverlight: "));
	client.setWriteHandler(new WriteToFileCurlWriteHandler(installerFile));
	client.setUrl("http://silverlight.dlservice.microsoft.com/download/E/4/4/E44E1840-4BBE-4CFE-AA06-E739131D6B7E/10411.00/runtime/Silverlight.exe");
    CurlResult result = client.executeCall();
    client.cleanWriteHandler();
    client.cleanStatisticsHandler();
    if(!result.isOkResponse()) {
    	message = "Non e' stato possibile scaricare il file di installazione di Silverlight.\n";
    	message += "Ti preghiamo di riprovare piu' tardi.";
    	MessageBox(NULL, message.c_str(), TEXT("Errore"), MB_OK | MB_ICONERROR);
    	return false;
    }

    // launch the installer and wait for the process to complete
    string quotedInstallerFile("\"");
    quotedInstallerFile += installerFile + "\"";
    launchProcess(quotedInstallerFile);

    DeleteFile(installerFile.c_str());

	return true;
}

bool moveWindowToForeground(string windowTitle) {
	HWND window = FindWindowEx(NULL, NULL, NULL, windowTitle.c_str());
	if(window != NULL) {
		DWORD hMyThread = GetCurrentThreadId();
		DWORD hOtherThread = GetWindowThreadProcessId(window, NULL);
		AttachThreadInput( hMyThread,hOtherThread,TRUE );
		SetForegroundWindow(window);
		AttachThreadInput( hMyThread,hOtherThread,FALSE);
	}
	return false;
}

bool downloadAndInstallFeezy(string silverlightFolder, string tempPath, CurlClient &client) {
	string feezyApp(tempPath + "\\FeezyApp.xap");

	client.setStatisticsHandler(new WindowsConsoleWriterStatisticsHandler("Percentuale download dell' applicazione Feezy: "));
	client.setWriteHandler(new WriteToFileCurlWriteHandler(feezyApp));
	client.setUrl("https://www.feezy.it/client/FeezyApp.xap");
	CurlResult result = client.executeCall();
	client.cleanWriteHandler();
	client.cleanStatisticsHandler();
	if(!result.isOkResponse()) {
		string message("Non e' stato possibile scaricare l'applicazione Feezy.\n");
		message += "Ti preghiamo di riprovare piu' tardi.";
		MessageBox(NULL, message.c_str(), TEXT("Errore"), MB_OK | MB_ICONERROR);
		return false;
	}

	// launch the silverlight executor to install the app
	string installCommandLine("\"");
	installCommandLine += silverlightFolder + "\\sllauncher.exe\" ";
	installCommandLine += "\"/install:" + feezyApp + "\" ";
	installCommandLine += "\"/origin:https://www.feezy.it/client/FeezyApp.xap\" ";
	installCommandLine += "/shortcut:desktop+startmenu";

	bool ret = launchProcess(installCommandLine);

	// search the link on the desktop
	TCHAR desktopFolderChars[MAX_PATH];
	ret = SUCCEEDED(SHGetFolderPath(NULL, CSIDL_DESKTOPDIRECTORY, NULL, 0, desktopFolderChars));
	string desktopFolder(desktopFolderChars);
	string linkName;
	if(ret) {
		WIN32_FIND_DATA ffd;
		string links(desktopFolder + "\\*.lnk");
		HANDLE hFind = INVALID_HANDLE_VALUE;
		hFind = FindFirstFile(links.c_str(), &ffd);
		ret = false;
		if(hFind != INVALID_HANDLE_VALUE) {
			do {
				if(strcasecmp(ffd.cFileName, "feezy.lnk") == 0) {
					ret = true;
					linkName = desktopFolder + "\\" + ffd.cFileName;
					break;
				}
			} while(FindNextFile(hFind, &ffd));
		}
		FindClose(hFind);
	}

	if(ret) {
		string message("Feezy e' ora installato sul tuo PC!\n");
		message += "Potrai utilizzare il link che e' stato creato sul desktop per iniziare ad ascoltare la tua musica.\n\n";
		message += "FEEZY. PERCHE' NOI AMIAMO LA MUSICA!\n\n";
		message += "Vuoi lanciare Feezy ora?";
		int result = MessageBox(NULL, message.c_str(), TEXT("Installazione completata"),
				                MB_YESNO | MB_ICONASTERISK);

		if(result == IDYES) {
			int launch = (int)ShellExecute(NULL, "open", linkName.c_str(), NULL, NULL, SW_SHOWNORMAL);
			if(launch > 32) { // launch has succeeded
				moveWindowToForeground("Feezy");
			}
		}
	}

	DeleteFile(feezyApp.c_str());
	return ret;
}

int getSilverlightVersionFromRegistry() {
	int version = 0;
	HKEY softwareKey = NULL;
	HKEY microsoftKey = NULL;
	HKEY silverlightKey = NULL;
	DWORD res = RegOpenKeyEx(HKEY_LOCAL_MACHINE, "SOFTWARE", 0, KEY_READ, &softwareKey);
	if (res == ERROR_SUCCESS) {
		res = RegOpenKeyEx(softwareKey, "Microsoft", 0, KEY_READ, &microsoftKey);
		if (res == ERROR_SUCCESS) {
			res = RegOpenKeyEx(microsoftKey, "Silverlight", 0, KEY_READ, &silverlightKey);
			if (res == ERROR_SUCCESS) {
				DWORD type;
				DWORD dataSize = MAX_PATH;
				BYTE data[MAX_PATH];
				res = RegQueryValueEx(silverlightKey, "Version", NULL, &type, data, &dataSize);
				if (res == ERROR_SUCCESS && type == REG_SZ) {
					vector<string> tokens = splitString((char*) data, ".");
					if (tokens.size() >= 2)
						version = atoi(tokens[0].c_str()) * 1000 + atoi(tokens[1].c_str());
				}
			}
		}
	}
	if (silverlightKey != NULL)
		RegCloseKey(silverlightKey);
	if (microsoftKey != NULL)
		RegCloseKey(microsoftKey);
	if (softwareKey != NULL)
		RegCloseKey(softwareKey);

	return version;
}

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
	CurlClient client;

	int minSilverlightVersion = 5000;

	// retrieve temp path
	TCHAR tempPathChar[MAX_PATH];
	if(GetEnvironmentVariable("TEMP", tempPathChar, MAX_PATH) == 0) {
		DisplayErrorBox("GetEnvironmentVariable");
		return 0;
	}
	string tempPath(tempPathChar);

	// retrieve the programs folder on the machine
	TCHAR programsFolderChars[MAX_PATH];
	SHGetFolderPath(NULL, CSIDL_PROGRAM_FILES, NULL, 0, programsFolderChars);
	string programsFolder(programsFolderChars);

	// check if the silverlight folder is present
	WIN32_FIND_DATA ffd;
	string silverlightFolder(programsFolder + "\\Microsoft Silverlight");
	HANDLE hFind = INVALID_HANDLE_VALUE;

	hFind = FindFirstFile(silverlightFolder.c_str(), &ffd);

	bool continueOnInstall = false;
	bool silverlightOK = false;

	if (INVALID_HANDLE_VALUE == hFind) {
		DWORD error = GetLastError();
		if(error == 2) { // file not found, we must download and install silverlight
			continueOnInstall = downloadAndInstallSilverlight(false, tempPath, client);
		}
		else
			DisplayErrorBox(TEXT("FindFirstFile"), error);
		FindClose(hFind);
	}
	else {
		FindClose(hFind);
		int version = getSilverlightVersionFromRegistry();
		if(version < minSilverlightVersion){
			continueOnInstall = downloadAndInstallSilverlight(true, tempPath, client);
		}
		else {
			continueOnInstall = true;
			silverlightOK = true;
		}
	}

	if(!silverlightOK) {
		// check if the user has really installed Silverlight by checking the registry entries
		int version = getSilverlightVersionFromRegistry();
		continueOnInstall = (version >= minSilverlightVersion);
	}

	if(continueOnInstall) {
		downloadAndInstallFeezy(silverlightFolder, tempPath, client);
	}

	return 0;
}
