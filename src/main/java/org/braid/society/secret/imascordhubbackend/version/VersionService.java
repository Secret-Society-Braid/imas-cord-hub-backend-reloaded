package org.braid.society.secret.imascordhubbackend.version;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class VersionService {

  private static final VersionHelper helper = new VersionHelper();
}
