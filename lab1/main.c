/*
 * @brief
 *
 * Originally download from Internet. Minor modification made.
 * */

#include<stdio.h>	//scanf , printf
#include<string.h>	//strtok
#include<stdlib.h>	//realloc
#include<sys/socket.h>	//socket
#include<netinet/in.h> //sockaddr_in
#include<arpa/inet.h>	//getsockname
#include<netdb.h>	//hostent
#include<unistd.h>	//close

int get_whatthe_data(char * , char **);
int hostname_to_ip(char * , char *);
int whatthe_query(char * , char * , char **);
char *str_replace(char *search , char *replace , char *subject );

int main(int argc , char *argv[])
{
	char domain[100] , *data = NULL;
	
	printf("Enter domain name : ");
	scanf("%s" , domain);
	
	get_whatthe_data(domain , &data);
	
	return 0;
}

int get_whatthe_data(char *domain , char **data)
{
	char ext[1024] , *pch , *response = NULL , *response_2 = NULL , *wch , *dt;
    
	domain = str_replace("http://" , "" , domain);
	domain = str_replace("www." , "" , domain);
    
	dt = strdup(domain);
	if(dt == NULL)
	{
		printf("strdup failed");
	}
	pch = (char*)strtok(dt , ".");
	while(pch != NULL)
	{
		strcpy(ext , pch);
		pch = strtok(NULL , ".");
	}
	
	if(whatthe_query("whois.iana.org" , ext , &response))
	{
		printf("Query failed");
	}
	
	pch = strtok(response , "\n");
	while(pch != NULL)
	{
		wch = strstr(pch , "whois.");
		if(wch != NULL)
		{
			break;
		}
        
		pch = strtok(NULL , "\n");
	}
    
	
	
	free(response);
	response = NULL;
	if(wch != NULL)
	{
		printf("\nTLD The server is : %s" , wch);
		if(whatthe_query(wch , domain , &response))
		{
			printf("The query failed");
		}
	}
	else
	{
		printf("\nTLD The server for %s not found" , ext);
		return 1;
	}
	
	response_2 = strdup(response);
    
	pch = strtok(response , "\n");
	while(pch != NULL)
	{
		wch = strstr(pch , "whois.");
		if(wch != NULL)
		{
			break;
		}
        
		//Next line please
		pch = strtok(NULL , "\n");
	}
    
    
	if(wch)
	{
		
		printf("\nRegistrar Whois server is : %s" , wch);
		
		if(whatthe_query(wch , domain , &response))
		{
			printf("The query failed");
		}
		
		printf("\n%s" , response);
	}
	else
	{
		printf("%s" , response_2);
	}
	return 0;
}

int whatthe_query(char *server , char *query , char **response)
{
	char ip[32] , message[100] , buffer[1500];
	int sock , read_size , total_size = 0;
	struct sockaddr_in dest;
    
	sock = socket(AF_INET , SOCK_STREAM , IPPROTO_TCP);
    
    //Prepare connection structures :)
    memset( &dest , 0 , sizeof(dest) );
    dest.sin_family = AF_INET;
    
	printf("\nResolving %s..." , server);
	if(hostname_to_ip(server , ip))
	{
		printf("Failed");
		return 1;
	}
	printf("%s" , ip);    
	dest.sin_addr.s_addr = inet_addr( ip );
	dest.sin_port = htons( 43 );
    
	//Now connect to remote server
	if(connect( sock , (const struct sockaddr*) &dest , sizeof(dest) ) < 0)
	{
		perror("connect failed");
	}
	
	//Now send some data or message
	printf("\nQuerying for ... %s ..." , query);
	sprintf(message , "%s\r\n" , query);
	if( send(sock , message , strlen(message) , 0) < 0)
	{
		perror("send failed");
	}
	
	//Now receive the response
	while( (read_size = recv(sock , buffer , sizeof(buffer) , 0) ) )
	{
		*response = realloc(*response , read_size + total_size);
		if(*response == NULL)
		{
			printf("realloc failed");
		}
		memcpy(*response + total_size , buffer , read_size);
		total_size += read_size;
	}
	printf("Done");
	fflush(stdout);
	
	*response = realloc(*response , total_size + 1);
	*(*response + total_size) = '\0';
	
	close(sock);
	return 0;
}

int hostname_to_ip(char * hostname , char* ip)
{
	struct hostent *he;
	struct in_addr **addr_list;
	int i;
    
	if ( (he = gethostbyname( hostname ) ) == NULL) 
	{
		herror("gethostbyname");
		return 1;
	}
    
	addr_list = (struct in_addr **) he->h_addr_list;
	
	for(i = 0; addr_list[i] != NULL; i++) 
	{
		strcpy(ip , inet_ntoa(*addr_list[i]) );
		return 0;
	}
	
	return 0;
}

char *str_replace(char *search , char *replace , char *subject)
{
	char  *p = NULL , *old = NULL , *new_subject = NULL;
	int c = 0 , search_size;
	
	search_size = strlen(search);
	
	for(p = strstr(subject , search) ; p != NULL ; p = strstr(p + search_size , search))
	{
		c++;
	}
	
	c = ( strlen(replace) - search_size )*c + strlen(subject);
	
	new_subject = malloc( c );
	
	strcpy(new_subject , "");
	
	old = subject;
	
	for(p = strstr(subject , search) ; p != NULL ; p = strstr(p + search_size , search))
	{
		strncpy(new_subject + strlen(new_subject) , old , p - old);
		
		strcpy(new_subject + strlen(new_subject) , replace);
		
		old = p + search_size;
	}
	
	strcpy(new_subject + strlen(new_subject) , old);
	
	return new_subject;
}
