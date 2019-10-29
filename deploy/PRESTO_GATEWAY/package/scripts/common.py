
def get_jvm_params(path):

    params = ''
    f = open(path, 'rb')
    for line in f:
        params += ' '
        params += line.strip('\n')
    f.close()
    return params